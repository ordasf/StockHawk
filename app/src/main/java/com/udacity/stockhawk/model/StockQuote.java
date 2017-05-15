package com.udacity.stockhawk.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockQuote implements Parcelable {

    private long id;
    private String symbol;
    private float price;
    private float absoluteChange;
    private float percentageChange;
    private String history;

    public StockQuote(long id, String symbol, float price, float absoluteChange, float percentageChange, String history) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.absoluteChange = absoluteChange;
        this.percentageChange = percentageChange;
        this.history = history;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getAbsoluteChange() {
        return absoluteChange;
    }

    public void setAbsoluteChange(float absoluteChange) {
        this.absoluteChange = absoluteChange;
    }

    public float getPercentageChange() {
        return percentageChange;
    }

    public void setPercentageChange(float percentageChange) {
        this.percentageChange = percentageChange;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockQuote that = (StockQuote) o;

        if (id != that.id) return false;
        if (Float.compare(that.price, price) != 0) return false;
        if (Float.compare(that.absoluteChange, absoluteChange) != 0) return false;
        if (Float.compare(that.percentageChange, percentageChange) != 0) return false;
        if (!symbol.equals(that.symbol)) return false;
        return history.equals(that.history);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + symbol.hashCode();
        result = 31 * result + (price != +0.0f ? Float.floatToIntBits(price) : 0);
        result = 31 * result + (absoluteChange != +0.0f ? Float.floatToIntBits(absoluteChange) : 0);
        result = 31 * result + (percentageChange != +0.0f ? Float.floatToIntBits(percentageChange) : 0);
        result = 31 * result + history.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "StockQuote{" +
                "id=" + id +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", absoluteChange=" + absoluteChange +
                ", percentageChange=" + percentageChange +
                ", history='" + history + '\'' +
                '}';
    }

    protected StockQuote(Parcel in) {
        id = in.readLong();
        symbol = in.readString();
        price = in.readFloat();
        absoluteChange = in.readFloat();
        percentageChange = in.readFloat();
        history = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(symbol);
        dest.writeFloat(price);
        dest.writeFloat(absoluteChange);
        dest.writeFloat(percentageChange);
        dest.writeString(history);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StockQuote> CREATOR = new Parcelable.Creator<StockQuote>() {
        @Override
        public StockQuote createFromParcel(Parcel in) {
            return new StockQuote(in);
        }

        @Override
        public StockQuote[] newArray(int size) {
            return new StockQuote[size];
        }
    };
}