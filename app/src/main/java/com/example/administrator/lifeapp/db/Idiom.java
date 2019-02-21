package com.example.administrator.lifeapp.db;

import java.util.List;

public class Idiom {
    private String idiom;
    private String chinesePhoneticize;
    private String grammar;
    private String from;
    private String explain;
    private String example;
    private String reason;
    private List<String> synonyms;
    private List<String> antonyms;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIdiom() {
        return idiom;
    }

    public void setIdiom(String idiom) {
        this.idiom = idiom;
    }

    public String getChinesePhoneticize() {
        return chinesePhoneticize;
    }

    public void setChinesePhoneticize(String chinesePhoneticize) {
        this.chinesePhoneticize = chinesePhoneticize;
    }

    public String getGrammar() {
        return grammar;
    }

    public void setGrammar(String grammar) {
        this.grammar = grammar;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    public List<String> getAntonyms() {
        return antonyms;
    }

    public void setAntonyms(List<String> antonyms) {
        this.antonyms = antonyms;
    }
}
