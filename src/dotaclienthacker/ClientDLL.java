package dotaclienthacker;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nexor
 */
public class ClientDLL {

    private static final byte[] EXPECTED_HEADER = new byte[]{'d', 'o', 't', 'a', '_', 'c', 'a', 'm', 'e', 'r', 'a', '_', 'd', 'i', 's', 't', 'a', 'n', 'c', 'e', '\0', '\0', '\0', '\0'};
    private static final byte[] EXPECTED_HEADER_2 = new byte[]{'d', 'o', 't', 'a', '_', 'c', 'a', 'm', 'e', 'r', 'a', '_', 'f', 'o', 'g', '_', 's', 't', 'a', 'r', 't', '_', 'z', 'o', 'o', 'm', 'e', 'd', '_', 'i', 'n', '\0'};
    private static ClientDLL c;
    private File dllFile;
    private byte[] byteContent;
    private int zoomIndex;
    private String zoom;

    private ClientDLL() {
        zoom = "";
        findDLLFile();
    }

    public static ClientDLL getClientDLL() throws FileNotFoundException, IOException {
        if (c == null) {
            c = new ClientDLL();
            DataInputStream dis = new DataInputStream(new FileInputStream(c.dllFile));
            c.byteContent = new byte[dis.available()];
            dis.read(c.byteContent);
            dis.close();
            c.findZoom();
        }
        return c;
    }

    private void findDLLFile() {
        try {
            Process p = Runtime.getRuntime().exec("reg query HKCR\\dota2\\Shell\\Open\\Command");
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String res = "";
            String aux;
            while ((aux = br.readLine()) != null) {
                res += aux;
            }
            String REGEX = "REG_SZ\\s+\"([^\"]+)";
            Matcher m = Pattern.compile(REGEX).matcher(res);
            String arch = System.getProperty("os.arch");
            boolean is64 = arch.contains("64") && !arch.contains("32");
            if (m.find()) {
                String dotaExecutablePathname = m.group(1);
                File dotaExecutable = new File(dotaExecutablePathname);
                File correctGameFolder = dotaExecutable.getParentFile()/*win64(1UP)*/.getParentFile()/*bin(1UP)*/.getParentFile()/*game(1UP)*/;
                dllFile = new File(correctGameFolder.getAbsolutePath() + File.separator
                        + "dota" + File.separator
                        + "bin" + File.separator
                        + "win" + (is64 ? "64" : "32")/*changes depending on arch*/ + File.separator
                        + "client.dll");
            }
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getZoom() {
        return zoom;
    }

    /**
     * finds the index of the zoom in the file and assigns the found String.
     */
    private void findZoom() {
        int headerCount = 0;
        for (int i = 0; i < c.byteContent.length; i++) {
            if (c.byteContent[i] == EXPECTED_HEADER[headerCount]) {
                headerCount++;
                if (headerCount == EXPECTED_HEADER.length) {
                    findZoom(i + 1);
                    if (!zoom.matches("\\d{3,4}")) {
                        zoom = "";//reset zoom bc its plain wrong u foktard
                        findZoom2();
                    }
                    break;
                }
            } else {
                headerCount = 0;
            }
        }
    }

    private void findZoom2() {
        int headerCount = 0;
        for (int i = 0; i < c.byteContent.length; i++) {
            if (c.byteContent[i] == EXPECTED_HEADER_2[headerCount]) {
                headerCount++;
                if (headerCount == EXPECTED_HEADER_2.length) {
                    findZoom(i + 1);
                    break;
                }
            } else {
                headerCount = 0;
            }
        }
    }

    /**
     * assigns the 4 digits successive to the found index of the expected header
     * to zoom variable (finding is done)
     *
     * @param index
     */
    private void findZoom(int index) {
        zoomIndex = index;
        for (int i = index; i < index + 4 /*char size of zoom*/; i++) {
            zoom += (char) c.byteContent[i];
        }
    }

    public void applyWantedZoom(String wantedZoom) throws IOException {
        System.out.println("Applying " + wantedZoom + " zoom... ");
        for (int i = 0; i < wantedZoom.length(); i++) {
            c.byteContent[zoomIndex + i] = (byte) wantedZoom.charAt(i);
        }
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(c.dllFile));
        dos.write(c.byteContent);
        dos.flush();
        dos.close();
        System.out.println("Done.");
    }
}
