package me.anhnguyen.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class AtmDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "me.anhnguyen.atmfinder.model.dao");
        schema.setDefaultJavaPackageTest("me.anhnguyen.atmfinder.test");

        Entity atm = schema.addEntity("Atm");
        atm.addIdProperty();
        atm.addStringProperty("name");
        atm.addStringProperty("address");
        atm.addDoubleProperty("lat");
        atm.addDoubleProperty("lon");

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
