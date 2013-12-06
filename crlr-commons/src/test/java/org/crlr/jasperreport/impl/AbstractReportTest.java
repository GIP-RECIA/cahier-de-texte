/*
 * Projet ENT-CRLR - Conseil Regional Languedoc Roussillon.
 * Copyright (c) 2009 Bull SAS
 *
 * $Id: AbstractReportTest.java,v 1.1 2009/05/26 07:34:42 ent_breyton Exp $
 */

package org.crlr.jasperreport.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.crlr.log.Log;
import org.crlr.log.LogFactory;
import org.crlr.utils.IOUtils;

/**
 * Classe abstraite de base pour les tests concernant les éditions.
 *
 * @author breytond.
 */
public abstract class AbstractReportTest extends TestCase {
    protected final Log log = LogFactory.getLog(getClass());

    /**
     * Chemin vers le report.
     * @param name le nom du report.
     * @return le chemin complet suivi du nom du report.
     */
    protected String getTemplate(String name) {
        final String prefix = "/org/crlr/report/";
        return prefix + name;
    }

    /**
     * Copie le report.
     * @param data les données.
     * @param file le fichier.
     * @throws IOException l'exception potentielle.
     */
    protected void dump(byte[] data, File file) throws IOException {
        final ReadableByteChannel buffer =
            Channels.newChannel(new ByteArrayInputStream(data));
        final FileChannel chan = new FileOutputStream(file).getChannel();
        try {
            long bytesWritten = 0;
            while (bytesWritten != data.length) {
                bytesWritten += chan.transferFrom(buffer, bytesWritten, 1024);
            }
        } finally {
            IOUtils.close(chan);
        }
    }

    /**
     * Liste des noms, pour test.
     * @return la liste.
     */
    protected List<TestDTO> createListeNom() {
        final List<TestDTO> liste = new ArrayList<TestDTO>();      
        liste.add(new TestDTO("BREYTON", "David", 30));
        liste.add(new TestDTO("WEBER", "Aurore", 22));
        liste.add(new TestDTO("CARRIERE", "Jerome", 21));
        liste.add(new TestDTO("VIBERT", "Dimitri", 25));

        return liste;
    }
}
