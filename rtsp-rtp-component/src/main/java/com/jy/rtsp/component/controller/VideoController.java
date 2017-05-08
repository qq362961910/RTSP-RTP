package com.jy.rtsp.component.controller;

/**
 * 视频控制器
 * */
public interface VideoController {
    /**
     * OPTIONS
     * */
    void doOption();

    /**
     * DESCRIBE
     * */
    void doDescribe();

    /**
     * SETUP
     * */
    void doSetup();

    /**
     * PLAY
     * */
    void doPlay();

    /**
     * PAUSE
     * */
    void doPause();

    /**
     * TEARDOWN
     * */
    void doTeardown();
}
