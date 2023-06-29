package com.poethan.jear.utils;

import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Iterator;

@NoArgsConstructor
public class EzString {
    public static String join(Collection<String> pieces, String separator) {
        StringBuilder buffer = new StringBuilder();
        for (Iterator<String> iter = pieces.iterator(); iter.hasNext(); ) {
            buffer.append( iter.next() );
            if ( iter.hasNext() )
                buffer.append( separator );
        }
        return buffer.toString();
    }

    public static String joins(Collection<Object> pieces, String separator) {
        StringBuilder buffer = new StringBuilder();
        for (Iterator<Object> iter = pieces.iterator(); iter.hasNext(); ) {
            buffer.append( iter.next() );
            if ( iter.hasNext() )
                buffer.append( separator );
        }
        return buffer.toString();
    }
}
