import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {                        
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { 
        
        // cria a rede neural
        Neural neural = new Neural("ENB2012_data");                                
        
        // realiza a seleção das classes
        neural.selecaoClasses();               
        
        // quantidade de iterações
        int iteracoes = 100000;
                
        //Inicializa matriz de pesos
        neural.createMatriz();
        
        //avalia os resultados do treinamento,
        //dando continuidade até o limite do treinamento.
        while(iteracoes>0){
            neural.avaliaTreinamento();            
            iteracoes--;
            System.out.println("CONTAGEM REGRESSIVA -> "+iteracoes);
        }        
    }

}
