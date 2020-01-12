/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModuleTXT;

import DAO.TypeVariable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leitep
 */
public class ReadTXT<T> {

    private static Field[] fields;

    private static void FillFields(Object obj) {
        fields = obj.getClass().getDeclaredFields();
    }

    public List<T> LerArquivoTXT(T obj, String path) throws FileNotFoundException, IOException, InstantiationException, IllegalAccessException, ParseException {

        BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
        String[] cabecalhos;
        List<T> list = new ArrayList<>();
        cabecalhos = reader.readLine().split(";");
        FillFields(obj);
        while (reader.ready()) {

            String[] linha = reader.readLine().split(";");
            if (linha.length == cabecalhos.length) {
                obj = (T) obj.getClass().newInstance();

                for (Field field : fields) {
                    field.setAccessible(true);
                    int match = 0;
                    for (int i = 0; i < cabecalhos.length; i++) {
                        if (cabecalhos[i].toUpperCase().equals(field.getName().toUpperCase())) {
                            match = i;
                        }

                    }
                    switch (field.getType().getTypeName()) {
                        case TypeVariable.StringType:
                            field.set(obj, linha[match]);
                            break;
                        case TypeVariable.IntType:
                            field.set(obj, Integer.parseInt(linha[match]));
                            break;
                        case TypeVariable.DoubleType:
                            field.set(obj, Double.parseDouble(linha[match]));
                            break;
                        case TypeVariable.FloatType:
                            field.set(obj, Float.parseFloat(linha[match]));
                            break;
                        case TypeVariable.LongType:
                            field.set(obj, Long.parseLong(linha[match]));
                            break;
                        case TypeVariable.Boolean:
                            field.set(obj, Boolean.parseBoolean(linha[match]));
                            break;
                        case TypeVariable.DateType:
                            String[] ano = linha[match].split("/");
                            if (ano[2].length() == 2) {
                                linha[match] = linha[match].substring(0, linha[match].length()-2) + "20" + linha[match].substring(linha[match].length()-2, linha[match].length());
                            }
                            java.util.Date d1 = new SimpleDateFormat("dd/MM/yyyy").parse(linha[match]);
                            field.set(obj, new java.sql.Date(d1.getTime()));
                            break;
                        case TypeVariable.DateTypeSql:
                            java.util.Date d = new SimpleDateFormat("dd/MM/YYYY").parse(linha[match]);
                            field.set(obj, new java.sql.Date(d.getTime()));
                            break;
                    }
                }
            }
            list.add(obj);
        }
        reader.close();
        return list;
    }
}
