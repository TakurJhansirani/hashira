import java.io.FileReader;
import java.util.*;
import java.math.BigInteger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HashiraPolynomial {

    public static BigInteger convertToDecimal(String value, int base) {
        return new BigInteger(value, base);
    }

    public static double[] lagrangeInterpolation(double[] xs, double[] ys, int k) {
        int n = k; 
        double[] coeffs = new double[n];

        for (int i = 0; i < n; i++) {
            double[] term = new double[n];
            term[0] = 1.0;
            double denom = 1.0;

            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                for (int d = n - 1; d >= 1; d--) {
                    term[d] = term[d] * (-xs[j]) + term[d - 1];
                }
                term[0] *= -xs[j];
                denom *= (xs[i] - xs[j]);
            }

            double factor = ys[i] / denom;
            for (int d = 0; d < n; d++) {
                coeffs[d] += term[d] * factor;
            }
        }

        return coeffs;
    }

    public static void main(String[] args) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(new FileReader("input.json"));

            JSONObject keys = (JSONObject) obj.get("keys");
            int n = ((Long) keys.get("n")).intValue();
            int k = ((Long) keys.get("k")).intValue();

            double[] xs = new double[k];
            double[] ys = new double[k];

            int count = 0;
            for (int i = 1; i <= n && count < k; i++) {
                JSONObject root = (JSONObject) obj.get(String.valueOf(i));
                if (root == null) continue;

                int base = Integer.parseInt((String) root.get("base"));
                String value = (String) root.get("value");

                BigInteger yBig = convertToDecimal(value, base);

                xs[count] = i;                  
                ys[count] = yBig.doubleValue(); 
                count++;
            }

            double[] coeffs = lagrangeInterpolation(xs, ys, k);

            System.out.println("Constant term (c): " + Math.round(coeffs[0]));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
