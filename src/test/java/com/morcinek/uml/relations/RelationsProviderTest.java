package com.morcinek.uml.relations;

import org.fest.assertions.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Copyright 2014 Tomasz Morcinek. All rights reserved.
 */
public class RelationsProviderTest {

    private RelationsProvider relationsProvider;

    @Before
    public void setUp() throws Exception {
        relationsProvider = new RelationsProvider();
    }

    @Test
    public void test1() throws Exception {
        Map<String, HashMap<String, Integer>> relations = relationsProvider.provideRelations("test-data/Test1");
        String string = relationsToString(relations);
        Assertions.assertThat(string).isEqualTo(
                "pl.edu.agh.morcinek.logic.Firma\n" +
                        " > pl.edu.agh.morcinek.logic.Pracownik : 13\n" +
                        "pl.edu.agh.morcinek.logic.Nauczyciel\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n" +
                        "pl.edu.agh.morcinek.logic.Osoba\n" +
                        " > pl.edu.agh.morcinek.logic.OsobaInterface : 32\n" +
                        "pl.edu.agh.morcinek.logic.Pracownik\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n" +
                        "pl.edu.agh.morcinek.logic.Student\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n" +
                        "pl.edu.agh.morcinek.logic.Uczelnia\n" +
                        " > pl.edu.agh.morcinek.logic.Student : 13\n" +
                        " > pl.edu.agh.morcinek.logic.Pracownik : 5\n" +
                        " > pl.edu.agh.morcinek.logic.Nauczyciel : 13\n"
        );
    }

    @Test
    public void test2() throws Exception {
        Map<String, HashMap<String, Integer>> relations = relationsProvider.provideRelations("test-data/Test2");
        String string = relationsToString(relations);
        Assertions.assertThat(string).isEqualTo(
                "pl.edu.agh.morcinek.logic.Osoba\n" +
                        " > pl.edu.agh.morcinek.logic.OsobaInterface : 32\n" +
                        "pl.edu.agh.morcinek.logic.Pracownik\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n"
        );
    }

    @Test
    public void test3() throws Exception {
        Map<String, HashMap<String, Integer>> relations = relationsProvider.provideRelations("test-data/Test3");
        String string = relationsToString(relations);
        Assertions.assertThat(string).isEqualTo(
                "pl.edu.agh.morcinek.logic.Osoba\n" +
                        " > pl.edu.agh.morcinek.logic.OsobaInterface : 32\n" +
                        "pl.edu.agh.morcinek.logic.Pracownik\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n"
        );
    }

    @Test
    public void test4() throws Exception {
        Map<String, HashMap<String, Integer>> relations = relationsProvider.provideRelations("test-data/Test4");
        String string = relationsToString(relations);
        Assertions.assertThat(string).isEqualTo(
                "pl.edu.agh.morcinek.logic.Osoba\n" +
                        " > pl.edu.agh.morcinek.logic.OsobaInterface : 32\n" +
                        "pl.edu.agh.morcinek.logic.Pracownik\n" +
                        " > pl.edu.agh.morcinek.logic.Osoba : 16\n" +
                        " > pl.edu.agh.morcinek.logic.Szef : 11\n" +
                        "pl.edu.agh.morcinek.logic.Szef\n" +
                        " > pl.edu.agh.morcinek.logic.Pracownik : 16\n"
        );
    }

    private String relationsToString(Map<String, HashMap<String, Integer>> relations) {
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> sortedKeys = new TreeSet<String>(relations.keySet());
        for (String mainName : sortedKeys) {
            HashMap<String, Integer> map = relations.get(mainName);
            stringBuilder.append(mainName);
            stringBuilder.append("\n");
            for (String name : map.keySet()) {
                stringBuilder.append(String.format(" > %s : %s", name, map.get(name)));
                stringBuilder.append("\n");
            }
        }
        return stringBuilder.toString();
    }
}
