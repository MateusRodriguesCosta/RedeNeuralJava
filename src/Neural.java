import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Neural {

    /**
     * Alocação de memória.
     */
    private int qnt_classe = 0;
    private int qnt_parametros = 0;
    
    private String[][] pesos;

    private Arquivo arquivo; //arquivo de texto

    private List<Objeto> objetoList = new ArrayList<>(); //lista de Objetos RNA

    private List<String> dataBase;
    
    private List<Objeto> valores; //saida esperada    

    private float[] esperada;
    
    private int acertos = 0;
    
    private int erros = 0;

    /**
     * Construtor
     *
     * @param dataBaseName nome da base de dados
     */
    public Neural(String dataBaseName) {
        this.arquivo = new Arquivo(dataBaseName);
        getData(); //le e retorna os valores da base de dados
        setValues(); //inicia os valores de parametros e classe                        
    }

    /**
     * Método que atribui os valores e os adicona na lista de Objeto RNA.
     * Ex.:
     *      Caso linha 0 coluna atual for igual X então valor é parametro
     *      Então, atribui ao valor identidade parametro e adiciona na
     *      objetoList.
     */
    private void setValues() {
        int numero_parametros = 0;
        int numero_classes = 0;
        //percorre toda a string vinda do arquivo de texto
        for (int i = 0; i < getData().size(); i++) {
            //System.out.println(getData().get(i));
            if (i == 0) { //le a primeira linha, ondes estão os valores de classe e parametro
                //System.out.println("linha 0");
                String[] data = getData().get(i).split(";"); //separa cada valor entre ';'
                for (String x : data) { //percorre o array dos objetos
                    if (x.contains("x") || x.contains("X")) { //verfica se é um PARAMETRO (X)
                        //cria um novo Objeto RNA do tipo PARAMETRO
                        //passando o tipo e o nome
                        Objeto parametro = new Objeto(Constantes.PARAMETER, x);
                        objetoList.add(parametro);
                        //ao percorrer todos os parametros o posicaBia guarda essa posicao
                        //1 posicao depois do ultimo parametro adicionado à lista de objetos
                        numero_parametros++;
                    }
                }
            }
        }
        //ao final adiciona um objeto BIA depois do ultimo parametro (X) adicionado
        Objeto bia = new Objeto(Constantes.BIA, "bia");
        
        //posicaoBia == 1 posicao depois do ultimo parametro adicionado
        int posicao_bia = numero_parametros;
        objetoList.add(posicao_bia, bia);
        qnt_classe = numero_classes;
        qnt_parametros = numero_parametros;
    }

    /**
     * Metodo que retorna uma string com 
     * todo o arquivo de texto Vindo da classe
     * Arquivo.
     *
     * @return String com todo o arquivo de texto
     */
    private ArrayList<String> getData() {
        return this.arquivo.getData();
    }

    /**
     * Metodo que retorna uma lista com todos os objetos adicionados Objetos
     * podem ser PARAMETRO, CLASSES ou BIA
     *
     * @return lista de objetos
     */
    public List<Objeto> getObjetosList() {
        return objetoList;
    }

    /**
     * @return the qnt_classe
     */
    public int getQnt_classe() {
        return qnt_classe;
    }

    /**
     * @return the qnt_parametros
     */
    public int getQnt_parametros() {
        return qnt_parametros;
    }

    /**
     * Cria a matriz com os pesos iniciais
     * em formato decimal e de valores aleatórios.
     *
     * @return
     */
    public String[][] createMatriz() {
        Random random = new Random();
        DecimalFormat df = new DecimalFormat("#.##");

        int row = this.getQnt_classe();
        int col = this.getQnt_parametros() + 1;
        

        String[][] matriz = new String[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                matriz[i][j] = df.format(random.nextFloat() * (1 - (-1)) + (-1));
            }
        }
        
        pesos = matriz; //armazena matriz pesos
        return matriz;
    }

    /**
     * Retona todos os valores da base de dados
     * discriminando a primeira linha que contém
     * X e Y.
     * @return
     */
    public List<String> getDataBase() {
        dataBase = new ArrayList<>();
        for (int i = 0; i < getData().size(); i++) {
            String[] text = getData().get(i).split("\n");
            for (String valor : text) {
                if (!valor.contains("X") && !valor.contains("Y")) {
                    dataBase.add(valor);
                    //System.out.println(valor);
                }
            }
        }
        return dataBase;
    }
    
    /**
     * Realiza a classificação das classes para transformar
     * de Regression em Classification.
     * Ex.:
     *      Caso valor menor ou igual a 25, então y igual 1
     *      Senão, y igual 2
     *      Daí, y1 concatenado com y2 = classe
     * @return 
     */
    public List<String> classificacao() {
        String l = null, c = null;
        List<String> lista = new ArrayList<>();
        for (int i = 0; i < getDataBase().size(); i++) {
            String[] linha = getDataBase().get(i).split(";");
            if (Float.parseFloat(linha[8].replace(",", ".")) <= 25) {
                l = "1";
            } else {
                l = "2";
            }
            if (Float.parseFloat(linha[9].replace(",", ".")) <= 25) {
                c = "1";
            } else {
                c = "2";
            }
            lista.add(l + "," + c);
        }
        return lista;
    }
    
    /**
     * Contabiliza as diferentes classes armazenando 
     * cada uma na lista de objetoList.
     * Ex.:
     *      array selecao{1.1, 2.1, 2.1, 3.3, 1.1}
     *      Resulta em 3 classes.
     */
    public void selecaoClasses() {
        List lista = classificacao();
        HashSet<String> selecao = new HashSet();
        selecao.addAll(lista);
        for (String string : selecao) {
            Objeto classe = new Objeto(Constantes.CLASS, string);
            objetoList.add(classe);
        }
        qnt_classe = selecao.size();
    }

    /**
     * Seleciona um par de treinamento do arquivo de texto
     * Ex.: 
     *      LINHA: 0,76;661,50;416,50;122,50;7,00;5;0,10;1;32,21;33,67
     * @return
     */
    public String selecionarParTreinamento() {
        Random r = new Random();
        int select = r.nextInt(getDataBase().size());        
        return getDataBase().get(select);
    }
    
    /**
     * (1)Atribui os valores aos parametros, bia e classes e 
     * (2)Estabelece os valores das saidas esperadas de cada clsse.
     * Ex(1).: x1 = 0.3, x2 = 1.0, bia = 1, y1 = 0.5, etc...
     * 
     * @return 
     */
    public List<Objeto> atribuiValores() {
        esperada = new float[qnt_classe];
        List<Objeto> objetos = new ArrayList<>();
        String[] arrayValues = selecionarParTreinamento().split(";");
        int cont = 0;
        for (int i = 0; i < getObjetosList().size(); i++) { // percorre lista de objetos para classificação
            if (getObjetosList().get(i).getTipo() == Constantes.PARAMETER) {
                Objeto o = getObjetosList().get(i);
                o.setDado(arrayValues[i]);
                objetos.add(o);
            } else if (getObjetosList().get(i).getTipo() == Constantes.BIA) {
                Objeto o = getObjetosList().get(i);
                o.setDado("1.0");
                objetos.add(o);
                //break;
            } else if (getObjetosList().get(i).getTipo() == Constantes.CLASS) {
                Objeto o = getObjetosList().get(i);
                
                for (int j = 0; j < esperada.length; j++) {
                    if (j == cont) {
                        esperada[j] = 1;
                    } else {
                        esperada[j] = -1;
                    }
                }
                o.setSE(esperada);
                objetos.add(o);
                cont++;
            }
        }
        return objetos;
    }
        
    /**
     * (1)Propaga os valores da camada x para a camada y e
     * (2)Gera saida da rede.
     * Ex(1).:
     *          x1*w1 + x2*w2* ... *xn*wn + bias*wbias
     * Ex(2).:
     *          saidaRede{-1, 1} ou saidaRede{1, -1)
     * @return 
     */
    public float[] propagaValores(){
        //valores resultado da propagação
        List<Float> saidas = new ArrayList<>();
        
        //valores resultado da rede
        float[] saidaRede;
        
        //valores recebidos do par de treinamento
        valores = atribuiValores();        
        
        //Realizando a somatoria e propagando x para y
        for (int linha = 0; linha < pesos.length; linha++) { // percorre a linha da matriz peso 
            float somatoria = 0;
            for (int coluna = 0; coluna < pesos[linha].length; coluna++) { // percorre a coluna da matriz peso                    
                    float x = Float.parseFloat(objetoList.get(coluna).getDado().replaceAll(",", "."));                    
                    float peso = Float.parseFloat(pesos[linha][coluna].replaceAll(",", "."));
                    
                    somatoria = somatoria + x*peso;                    
            }
            saidas.add(somatoria);
        }        
        
        //Verificando valores da saida e gerando saida da rede
        saidaRede = new float[saidas.size()];
        int index = 0;
        for (Float resultado : saidas) {
            if(resultado > 0){
                float valor = 1;
                saidaRede[index] = 1;
            }else{
                float valor = -1;
                saidaRede[index] = -1;
            }
            index++;
        }        
        return saidaRede;
    }
    
    /**
     * (1)Compara a saida da rede com a saida esperada
     * (2)Verifica se é necessário recalcular os pesos
     * (3)Escolhe outro par de treinamento.
     */
    public void avaliaTreinamento(){
        // valores resultado da rede
        float[] saidaRede = propagaValores();

        // condição de atualização de pesos
        boolean flag = false;

        for (Objeto saida : valores) { // percorre a lista de objetos (classes, parametros e bias)
            if(saida.getTipo() == Constantes.CLASS){
                flag = Arrays.equals(saidaRede, saida.getSE());
                System.out.println("COMPAROU: "+Arrays.toString(saidaRede));
                System.out.println("COM: "+Arrays.toString(saida.getSE()));
                if(flag == true){               
                    break;
                }
            }
        }
        
        /**
         * pesos         = matriz de pesos,
         * valores       = vetor saida esperada(entre classes, parametros e bias),
         * saidaRede     = vetor saida rede,
         * peso novo(ij) = peso atual(ij) + (saida esperada - saida rede(i)) * x(j) * bias.
         * 
         */                
        if(flag == false){ // se não for saida correta            
            // valores de y apenas
            List<Objeto> out = new ArrayList<>();
            
            // valor do bias
            float bias = 1;
        
            for(Objeto y : valores){ // armazena apenas os valores de classes existentes
                if(y.getTipo() == Constantes.CLASS) out.add(y);
            }
        
            for (int linha = 0; linha < out.size(); linha++) { // percorre as linhas (classes)
                float yi = Float.parseFloat(out.get(linha).getNome().replaceAll(",", "."));
                
                for (int coluna = 0; coluna < pesos[linha].length; coluna++) { //percorre a coluna de pesos                    
                    float pesoAtual = Float.parseFloat(pesos[linha][coluna].replaceAll(",", "."));                    
                    float xj = Float.parseFloat(valores.get(coluna).getDado().replaceAll(",", "."));
                    
                    // atualização do peso
                    pesoAtual = pesoAtual + (yi - saidaRede[linha]) * bias * xj;                     
                    String pesoNovo = String.valueOf(pesoAtual);                    
                    
                    this.pesos[linha][coluna] = String.valueOf(pesoAtual).replaceAll("\\.", ",");
                }
            }              
            this.erros++;
        }else{ // se for saida correta
            this.acertos++;
        }        
        System.out.println("ERROS: "+this.erros+" | ACERTOS: "+this.acertos);
    }    
}