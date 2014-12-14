package com.github.rbrugier;

import com.github.rbrugier.gen.FooFactory;

public class Main {

    public static void main(String[] args) {
        Foo foo = FooFactory.create();
        foo.print();
    }
}
