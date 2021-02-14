import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Arquivo {

    private FileInputStream fileInputStream;
    private Scanner scanner;
    private String[] contentFile;
    private String file_name;
    private File file;

    //Dentro dessa pasta esta o arquivo de texto
    private String path_data_base = null;

    /**
     * Construtor da classe
     *
     * @param file_name
     */
    public Arquivo(String file_name) {
        this.file_name = file_name;
        this.path_data_base = System.getProperty("user.dir") + "\\data_base\\" + this.file_name + ".txt";
    }

    /**
     * Construtor vazio.
     */
    public Arquivo() {
    }

    /**
     * Metodo que le o arquivo de texto e guarda as variaveis
     * como String para uso posterior.
     */
    public ArrayList<String> getData() {
        try {
            FileReader arq = new FileReader(path_data_base);
            BufferedReader lerArq = new BufferedReader(arq);
            String linha;
            ArrayList<String> data = new ArrayList<>();
            while ((linha = lerArq.readLine()) != null) {
                data.add(linha);
                //linha = lerArq.readLine();
                
            }
            if(arq != null){
                lerArq.close();
            }
            return data;
        } catch (IOException e) {
            return null;
        }
    }
}
