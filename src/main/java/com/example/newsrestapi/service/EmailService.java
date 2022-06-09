package com.example.newsrestapi.service;

import com.example.newsrestapi.model.AppUser;

import java.net.MalformedURLException;

public interface EmailService {
    public void sendEmailAboutAnnouncementPublication(AppUser applicationUser) throws MalformedURLException;
    public void sendEmailAboutAnnouncementEnd(AppUser applicationUser);
}
