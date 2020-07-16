package com.spring.demo.xml.service;

/**
 * @author HuSen
 * @since 2020/7/10 9:50 上午
 */
public class SimpleMovieLister {

    private final MovieFinder movieFinder;

    public SimpleMovieLister(MovieFinder movieFinder) {
        this.movieFinder = movieFinder;
    }

    public void test() {
        System.out.println("SimpleMovieLister.movieFinder: " + movieFinder);
    }
}
