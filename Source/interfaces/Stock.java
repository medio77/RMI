package interfaces;

import java.util.List;


public interface Stock {
    static final long VersionUID = 1234567L;
    
    public int sum(int a, int b, int c);
    public int getPrice(String symbol);
    public List<Boolean> CheckOpenness(List symbols);
    public List<List<String>> getOrder(String symbol,int count);
    
}
