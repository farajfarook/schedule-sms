package com.enbiso.proj.schedulesms.data;

import java.util.List;
import java.util.Objects;

/**
 * Created by farflk on 3/24/2014.
 */
public class SearchEntry {

    public enum Type{
        STRING, NUMBER
    }

    public enum Search{
        LIKE, IN, GREATER_THAN, LESS_THAN, EQUAL, NOT_EQUAL
    }

    private Type type;
    private Search search;
    private String name;
    private Object value;

    public SearchEntry(Type type, String name, Search search, Object value) {
        this.type = type;
        this.search = search;
        this.name = name;
        this.value = value;
    }

    public Object getValue(){
        return this.value;
    }

    public String getValueParsed(){
        if(search == Search.IN) {
            StringBuilder builder = new StringBuilder();
            int argCount = ((List)value).size();
            for (int i = 0; i < argCount; i++) {
                if(type == Type.STRING){
                    builder.append("'" + ((List) value).get(i) + "'");
                }else {
                    builder.append(((List) value).get(i));
                }
                if(i+1 != argCount){
                    builder.append(",");
                }
            }
            return builder.toString();
        }else if(type == Type.STRING){
            return "'" + value.toString() + "'";
        }else{
            return value.toString();
        }
    }

    public String toStringWithValue(){
        switch (search){
            case LIKE:
                return name + " LIKE " + getValueParsed();
            case GREATER_THAN:
                return name + " > " + getValueParsed();
            case LESS_THAN:
                return name + " < " + getValueParsed();
            case IN:
                return name + " IN (" + getValueParsed() + ")";
            case EQUAL:
                return name + " = " + getValueParsed();
            case NOT_EQUAL:
                return name + " != " + getValueParsed();
            default:
                return "";
        }
    }

    private String getInPlaceHolders(){
        StringBuilder placeHolderBuilder = new StringBuilder();
        int argCount = ((List)value).size();
        for (int i = 0; i < argCount; i++) {
            placeHolderBuilder.append("?");
            if(i+1 != argCount){
                placeHolderBuilder.append(",");
            }
        }
        return placeHolderBuilder.toString();
    }

    @Override
    public String toString() {
        switch (search){
            case LIKE:
                return name + " LIKE ? ";
            case GREATER_THAN:
                return name + " > ? ";
            case LESS_THAN:
                return name + " < ? ";
            case IN:
                return name + " IN (" + getInPlaceHolders() + ") ";
            case EQUAL:
                return name + " = ? ";
            case NOT_EQUAL:
                return name + " != ? ";
            default:
                return "";
        }
    }
}
