package com.fiospace.bigclock.datasources.homebrew;
import java.util.Iterator;
import java.util.List;

public class ForecastDataIterator implements Iterator<ForecastData.ForecastEntry> {

    private List<ForecastData.ForecastEntry> entries;
    private int currentIndex = 0;

    public ForecastDataIterator(List<ForecastData.ForecastEntry> entries) {
        this.entries = entries;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < entries.size();
    }

    @Override
    public ForecastData.ForecastEntry next() {
        if (!hasNext()) {
            throw new IndexOutOfBoundsException("No more elements in the list.");
        }
        return entries.get(currentIndex++);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove operation is not supported.");
    }
}