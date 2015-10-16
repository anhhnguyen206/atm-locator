package me.anhnguyen.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class AtmDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "me.anhnguyen.atmfinder.model.dao");
        schema.setDefaultJavaPackageTest("me.anhnguyen.atmfinder.test");

        Entity product = schema.addEntity("Atm");
        product.addIdProperty();
        product.addStringProperty("name");
        product.addDoubleProperty("lat");
        product.addDoubleProperty("lon");

        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }
}
