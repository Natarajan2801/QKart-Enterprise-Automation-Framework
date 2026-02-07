package com.qkart.utils;

import org.openqa.selenium.By;

public class DynamicXpath {
    public static By get(String xpath, String value) {
        return By.xpath(String.format(xpath, value));
    }
}