package com.whty.smartpos.unionpay.pay.msgmanager.model;

import android.content.res.AssetManager;
import android.util.Log;

import com.whty.smartpos.unionpay.pay.msgmanager.utils.XmlParser;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 格式查看表类
 *
 * @author Kenshino
 */
public class PatternTables {
    private static final Map<String, List<?>> PATTERN_TABLES = new HashMap<String, List<?>>();

    public static Map<String, List<?>> getPatternTables() {
        return PATTERN_TABLES;
    }

    public static List<?> getPattern(String key) {
        return PATTERN_TABLES.get(key);
    }

    public static void loadPatterns(AssetManager am, String[] keys,
                                    String[] paths) throws ParserConfigurationException,
            SAXException,
            IOException {
        if (am == null)
            return;

        if (keys == null)
            return;

        if (paths == null)
            return;

        if (keys.length != paths.length) {
            throw new IllegalArgumentException("参数长度不一致");
        }

        XmlParser parser = new XmlParser();

        for (int i = 0; i < keys.length; i++) {
            if (keys[i] != null && !"".equals(keys[i]) && paths[i] != null
                    && !"".equals(paths[i])) {
                InputStream is = getAssetsFileStream(am, paths[i]);
                List<?> list = parser.parseXML(is);
                Log.d("PatternTables", "KEY:" + keys[i] + " PATTERN: " + list.toString());
                PatternTables.getPatternTables().put(keys[i], list);
            }
        }
    }

    private static InputStream getAssetsFileStream(AssetManager assetManager,
                                                   String filePath) {
        InputStream in = null;
        try {
            in = assetManager.open(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return in;
    }

    public static void loadPattern(String key, InputStream is) throws ParserConfigurationException, SAXException, IOException {
        XmlParser parser = new XmlParser();
        List<?> list = parser.parseXML(is);
        Log.d("PatternTables", "KEY:" + key + " PATTERN: " + list.toString());
        PatternTables.getPatternTables().put(key, list);
    }
}
