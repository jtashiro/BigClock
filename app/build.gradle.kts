import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    id("com.github.triplet.play") version "4.1.1"
}

// Load keystore properties (created by CI from secrets) if present — parse manually to avoid java.* imports
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProps: Map<String, String> = if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.readLines()
        .map { it.trim() }
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .mapNotNull { line ->
            val idx = line.indexOf('=')
            if (idx > 0) {
                val key = line.substring(0, idx).trim()
                val value = line.substring(idx + 1).trim()
                key to value
            } else null
        }
        .toMap()
} else emptyMap()

fun keystoreProp(key: String, default: String? = null): String? = keystoreProps[key] ?: default

android {
    namespace = "com.fiospace.bigclock"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.fiospace.bigclock"
        minSdk = 26
        targetSdk = 36
        versionCode = getVersionCode()
        versionName = getVersionName()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val storeFilePath = keystoreProp("storeFile") ?: "keystore.jks"
            storeFile = file(storeFilePath)
            storePassword = keystoreProp("storePassword")
            keyAlias = keystoreProp("keyAlias")
            keyPassword = keystoreProp("keyPassword")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

// Computed once per configuration pass and reused by both getVersionCode() and
// getVersionName() so we don't double-bump VERSION_CODE when both are called.
//
// CI invokes Gradle multiple times per release (build, publish, then printVersion
// for the git tag): only the invocation that passes -PbumpVersion=true (the actual
// build step) should advance and persist VERSION_CODE. Every other invocation —
// including local `printVersion` checks and CI's tag/publish steps — just reads
// the value already on disk, so the code that ships matches what gets tagged.
var resolvedVersionCode: Int? = null

fun getVersionCode(): Int {
    resolvedVersionCode?.let { return it }

    val versionPropsFile = file("version.properties")
    if (!versionPropsFile.exists()) {
        versionPropsFile.createNewFile()
        versionPropsFile.writeText("VERSION_CODE=1\nVERSION_MAJOR=1\nVERSION_MINOR=0\nVERSION_PATCH=0\n")
    }

    val versionProps = Properties().apply {
        load(FileInputStream(versionPropsFile))
    }

    val currentCode = versionProps.getProperty("VERSION_CODE")?.toIntOrNull() ?: 0

    val code = if (project.hasProperty("bumpVersion")) {
        val bumped = currentCode + 1
        versionProps["VERSION_CODE"] = bumped.toString()

        val buildDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())
        versionProps["BUILD_DATE"] = buildDateTime

        versionProps.store(FileOutputStream(versionPropsFile), null)

        bumped
    } else {
        currentCode
    }

    resolvedVersionCode = code
    return code
}

fun getVersionName(): String {
    val versionPropsFile = file("version.properties")
    if (!versionPropsFile.exists()) {
        throw GradleException("version.properties file not found")
    }

    val versionProps = Properties().apply {
        load(FileInputStream(versionPropsFile))
    }

    val versionMajor = versionProps.getProperty("VERSION_MAJOR")?.toIntOrNull() ?: 1
    val versionMinor = versionProps.getProperty("VERSION_MINOR")?.toIntOrNull() ?: 0
    val versionPatch = versionProps.getProperty("VERSION_PATCH")?.toIntOrNull() ?: 0

    return "$versionMajor.$versionMinor.$versionPatch"
}

tasks.register("printVersion") {
    notCompatibleWithConfigurationCache("reads version.properties and the Android extension at configuration time")
    doLast {
        println("Version Code: ${android.defaultConfig.versionCode}")
        println("Version Name: ${getVersionName()}")
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.play.services.location)
    implementation(libs.preference)
    //implementation(files("/Users/jtashiro/AndroidStudioProjects/bitcoin_price_fetcher2/build/libs/bitcoin_price_fetcher-1.0.jar"))
    //implementation(fileTree(dir: 'libs', include: ['*.jar']))
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(libs.javax.mail.api)


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

// Configure Gradle Play Publisher to use a service account JSON written by CI at runtime
play {
    // CI will create `play-service-account.json` in the repo root during the workflow
    serviceAccountCredentials.set(rootProject.file("play-service-account.json"))
    // Publish app bundles by default (AAB)
    defaultToAppBundles.set(true)
    // Default track for automated publishing. Start on the closed testing track ("alpha" in
    // the Play Developer API / GPP's track vocabulary; Play Console's UI labels it "Closed
    // testing") until the app has enough tester activity for production. CI's
    // workflow_dispatch also accepts a `track` input to override this per-run.
    track.set("alpha")
}
