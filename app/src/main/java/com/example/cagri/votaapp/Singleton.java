package com.example.cagri.votaapp;

/**
 * Created by cagri on 27/11/2016.
 */

public class Singleton {
    private static Singleton _instance;

    private static Candidate mayor;
    private static Candidate councilman;
    private static Candidate detail;
    private static String electorId;

    private Singleton() {

    }

    public synchronized static Singleton getInstance() {
        if (_instance == null) {
            _instance = new Singleton();
        }
        return _instance;
    }

    public static Candidate getMayor() {
        return mayor;
    }

    public static void setMayor(Candidate mayor) {
        Singleton.mayor = mayor;
    }

    public static Candidate getCouncilman() {
        return councilman;
    }

    public static void setCouncilman(Candidate councilman) {
        Singleton.councilman = councilman;
    }

    public static Candidate getDetail() {
        return detail;
    }

    public static void setDetail(Candidate detail) {
        Singleton.detail = detail;
    }

    public static String getElectorId() {
        return electorId;
    }

    public static void setElectorId(String electorId) {
        Singleton.electorId = electorId;
    }
}