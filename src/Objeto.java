import java.util.List;
/**
 * Essa classe representa um objeto dentro da RNA (Rede Neural) que podem ser três
 * tipos: PARAMETRO, CLASSE, BIA.
 *
 * @author Kevin Mitnik
 */
public class Objeto {

    /**
     * Alocação de memória.
     */
    private String dado;
    private String nome; //nome do objeto
    private String description; //descrição
    private int tipo; // tipo (classe, parametro, bia)
    private final float BIA_VALOR;
    
    private List<String> valores;
    
    private float[] SE;

    /**
     * Construtor
     *
     * @param tipo classe; parametro; bia
     * @param parameterName nome
     */

    Objeto(int tipo, String parameterName) {
        this.BIA_VALOR = 0.1f;
        this.nome = parameterName;
        this.tipo = tipo;
    }    

    /**
     * @return nome do objeto
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome nome do objeto
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return descricao do objeto
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description descricao do objeto
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the tipo (classe, parametro, bia)
     */
    public int getTipo() {
        return tipo;
    }

    /**
     * @param tipo tipo (classe, parametro, bia)
     */
    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return this.nome;
    }

    /**
     * @return the BIA_VALOR
     */
    public float getBIA_VALOR() {
        if (this.tipo == Constantes.BIA) {
            return BIA_VALOR;
        } else {
            //para objetos != de BIA
            return -00;
        }
    }

    /**
     * @return the valores
     */
    public List<String> getValores() {
        return valores;
    }

    /**
     * @param valores the valores to set
     */
    public void setValores(List<String> valores) {
        this.valores = valores;
    }

    /**
     * @return the dado
     */
    public String getDado() {
        return dado;
    }

    /**
     * @param dado the dado to set
     */
    public void setDado(String dado) {
        this.dado = dado;
    }

    /**
     * @return the SE
     */
    public float[] getSE() {
        return SE;
    }

    /**
     * @param SE the SE to set
     */
    public void setSE(float[] SE) {
        this.SE = SE;
    }
}