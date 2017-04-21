package com.poos.cop.schedulebuilder;

/**
 * Created by timothy on 3/29/17.
 */

public class SectionPair implements Comparable<SectionPair> {
    String term;
    int nbr;
    int hash;

    public SectionPair(String term, int nbr) {
        this.term = term;
        this.nbr = nbr;
        hash = (term + "," + nbr).hashCode();
    }

    public SectionPair(Section section) {
        this(section.term, section.nbr);
    }

    public int hashCode() {
        return hash;
    }

    public int compareTo(SectionPair sectionPair) {
        int res = term.compareTo(sectionPair.term);
        if (res == 0) res = nbr - sectionPair.nbr;
        return res;
    }

    public boolean equals(Object o) {
        if(!(o instanceof SectionPair))
            return false;
        SectionPair sp = (SectionPair) o;
        return nbr == sp.nbr && term.equals(sp.term);
    }

    public String toString() {
        return term + "," + nbr;
    }
}
