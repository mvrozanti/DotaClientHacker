package dotaclienthacker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Nexor
 */
public class ClientDLL {

    private static final File CLIENT_DLL = new File("D:\\Steam\\SteamApps\\common\\dota 2 beta\\game\\dota\\bin\\win64\\client.dll");
    private static final String DOTA_CAMERA_DISTANCE_REGEX = "dota_camera_distance\\W*?(\\d+)";
    private static final byte[] EXPECTED_HEADER = new byte[]{'d', 'o', 't', 'a', '_', 'c', 'a', 'm', 'e', 'r', 'a', '_', 'd', 'i', 's', 't', 'a', 'n', 'c', 'e', '\0', '\0', '\0', '\0'};
    private static final String DEFAULT_ZOOM = "1134";
    private static ClientDLL c;
    private byte[] byteContent;
//    private String content;
    private int zoomIndex;
    private String zoom;

    private ClientDLL() {
        zoom = "";
    }

    public static ClientDLL getClientDLL() throws IOException {
        if (c == null) {
            c = new ClientDLL();
            DataInputStream dis = new DataInputStream(new FileInputStream(CLIENT_DLL));
            c.byteContent = new byte[dis.available()];
            dis.read(c.byteContent);
            dis.close();
            c.findZoom();
        }
        return c;
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
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(CLIENT_DLL));
        dos.write(c.byteContent);
        dos.flush();
        dos.close();
//        System.out.println("Input file hash = " + content.hashCode());
//        content = content.replaceFirst(DOTA_CAMERA_DISTANCE_REGEX, "dota_camera_distance\0\0\0\0" + wantedZoom);
//        System.out.println("Output file hash = " + content.hashCode());
//        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(CLIENT_DLL), "CP1252"))) {
//            bw.write(content);
//            bw.flush();
//            bw.close();
//        }
        System.out.println("Done.");
    }

}
