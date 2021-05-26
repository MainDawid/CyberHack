import java.util.Random;

public class Game {

    private final ElementValue[][] matrixValues;
    private final int matrixSize;

    public Game(int bufferSize)
    {
        matrixSize = bufferSize;
        matrixValues = new ElementValue[matrixSize][matrixSize];
        initMatrixValues();
    }

    public int getMatrixSize()
    {
        return matrixSize;
    }

    public void initMatrixValues()
    {
        Random randomValue = new Random();
        for(int i=0; i<getMatrixSize(); i++)
        {
            for(int j=0; j<getMatrixSize(); j++)
            {
                matrixValues[i][j] = getRandomValue(randomValue);
            }
        }
    }

    public ElementValue getMatrixValue(int i, int j)
    {
        return matrixValues[i][j];
    }

    public ElementValue getRandomValue(Random randomValue)
    {
        return ElementValue.values()[randomValue.nextInt(ElementValue.values().length)];
    }

}
