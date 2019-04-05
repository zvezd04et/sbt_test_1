package com.z;

public class Main {

    public static void main(String[] args) {
        Company company = Company.getInstance();
        company.doRefresh();
    }
}
