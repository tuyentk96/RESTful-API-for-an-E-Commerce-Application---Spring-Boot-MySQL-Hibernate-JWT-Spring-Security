package com.project.ShopApp.services;

import java.io.IOException;

public interface ExportDataService {
    void exportCategories() throws IOException;

    void exportProducts() throws IOException;
}
