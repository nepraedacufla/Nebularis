package org.example;
import java.text.DecimalFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Coords {
    //1
    public static double radParahora(double rad) {
        double h = ((rad * 180) / (15 * Math.PI));
        if (h > 24.0) {
            h = h - 24.0;
        }
        if (h < 0.0) {
            h = 24.0 + h;
        }
        return h;
    }

    //2
    public static double radStrParaGrau(String rad) {

        String x = "^(-?)[0-9]{1}\\.[0-9]{4,8}";
        Pattern comparando = Pattern.compile(x);
        Matcher matcher = comparando.matcher(rad);


        if (matcher.matches()) {
            double r = Double.parseDouble(rad);


            if (r < 0) {
                r = (2 * Math.PI) - Math.abs(r);
            }


            return (r * 180) / Math.PI;
        }
        else {
            return 0; //era para ser None mas não consegui :)
        }
    }
    //3
    public static String radPararadStr(double rad) {
        if (rad < 0) {
            return Double.toString(rad);
        }
        else {
            return String.format("%+.1f", rad);
        }
    }
    //4
    public static String degParadegStr(double deg) {
        double nd = Math.floor(deg);
        double nmins = (deg - nd) * 60;
        double mins = Math.floor(nmins);
        int secs = Math.round((float)((nmins - mins) * 60));

        return String.format("%dº%d'%d''", (int)nd, (int)mins, secs);

    }
    //5
    public static String radStrParadegStr(String x) {
        return degParadegStr(radStrParaGrau(x));
    }
    //6
    public static Double degStrPararad(String x) {
        Double d_ndeg = null;

        String x1 = "^-?[0-9]{1,3}(º|ᵒ)[0-9]{1,3}\\'[0-9]{1,3}([\\']{2}|\\\")$";
        Pattern comparando = Pattern.compile(x1);

        String x2 = "^-?[0-9]{1,3}\\.[0-9]{1,6}(º|ᵒ)$";
        Pattern comparando2 = Pattern.compile(x2);

        Matcher matcher1 = comparando.matcher(x);
        Matcher matcher2 = comparando2.matcher(x);

        if (!matcher1.matches() && !matcher2.matches()) {
            System.out.println("Erro de entrada:" + x);
            return null;
        }

        if (matcher1.matches()) {
            x = x.replace("º", ".").replace("''", ".").replace("'", ".");
            String[] d_dic = x.split("\\.");

            if (d_dic.length != 3) {
                System.out.println("Erro ao quebrar os componentes de grau, minuto e segundo.");
                return null;
            }

            double d_deg = Double.parseDouble(d_dic[0]);
            double d_min = Double.parseDouble(d_dic[1]);
            double d_sec = Double.parseDouble(d_dic[2]);

            if (d_deg < 0) {
                d_min = -d_min;
                d_sec = -d_sec;
            }

            d_ndeg = d_deg + (d_min / 60.0) + (d_sec / 3600.0);
        } else if (matcher2.matches()) {
            x = x.replace("º", "").replace("ᵒ", "");
            d_ndeg = Double.parseDouble(x);
            if (d_ndeg < 0) {
                d_ndeg = 360.0 - Math.abs(d_ndeg);
            }
        }
        return Math.toRadians(d_ndeg);
    }
    //7
    public static Double[] h_m_s(Double hora) {
        double h = Math.floor(hora);
        double hm = (hora - h) * 60;
        double m = Math.floor(hm);
        double s = (hm - m) * 60;

        // Não arredondar nada, só corrigir overflow se necessário
        if (s >= 60.0) {
            s = 0;
            m += 1;
        }
        if (m >= 60.0) {
            m = 0;
            h += 1;
        }

        Double[] resultado = {h, m, s};
        return resultado;
    }
    //8
    public static String horaParahoraStr(Double hora) {
        Double[] hora1 = h_m_s(hora);
        double h = hora1[0];
        double m = hora1[1];
        double s = hora1[2];
        return String.format("%dh%dm%ds", (int)h, (int)m, (int)s);
    }
    //9
    public static double[] d_m_s(double d){
        boolean tn = false;
        if (d<0){
            tn = true;
        }
        double d1 = Math.abs(d);
        double d2 = Math.floor(d1);
        double dm = (d1 - d2) * 60;
        double m = Math.floor(dm);
        double s = (dm - m)*60;
        if (s > 59.95){
            s = 0;
            m += 1;
        }
        if (m >= 60){
            m = 60 - m;
            d2 += 1;
        }
        if (tn){
            d2 = -d2;
        }
        double[] grau = {d2,m,s};
        return grau;
    }

    public static String[] eC_para_str(double r, double d, double m){
        double rh = r * (12.0 / 2147483648.0);
        double dd = d * 90.0/1073741824.0;
        double t = Math.floor(m / 1000000.0);
        Double[] x1 = h_m_s(rh);
        double[] x2 = d_m_s(dd);
        String x1Str = String.format("%dh%dm%ds", (int)(double)x1[0], (int)(double)x1[1], (int)(double)x1[2]);
        String x2Str = String.format("%dº%d'%d''", (int)(double)x2[0], (int)(double)x2[1], (int)(double)x2[2]);
        //faltou implementar uma função mtime
        String[] ret = {x1Str, x2Str};
        return ret;
    }
    public static String J2000(double ra, double d, double t){
        double rh = ra * 12.0/2147483648.0;
        Double[] x = h_m_s(rh);
        double h1 = (double) x[0];
        double m1 = (double) x[1];
        double s1 = (double) x[2];
        double dd = d * 90.0/1073741824;
        double[] x2 = d_m_s(dd);
        double h2 = (double) x2[0];
        double m2 = (double) x2[1];
        double s2 = (double) x2[2];
        double t2 = Math.floor(t/1000000);
        Instant instant = Instant.ofEpochSecond((long) t2);
        String formattedDate = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss yyyy").withZone(ZoneId.systemDefault()).format(instant);
        return String.format("%dh%dm%ds/%dº%d'%d'' em "+ formattedDate, (int)(double)h1, (int)(double)m1, (int)(double)s1, (int)(double)h2, (int)(double)m2, (int)(double)s2);

    }

    public static double[] rad_para_sp(double ra, double d){
        double rh = radParahora(ra);
        double dd = d * 180 / Math.PI;

        //logger.fine(String.format("(horas, grau): %d,%d", (int)(double) rh, (int)(double) dd)); não sei fazer
        double[] x = {(rh*(2147483648.0/12.0)),(dd*1073741824/90.0)};
        return(x);
    }
    //fazer sair sem notação cientirfica
    public static void main(String[] args) {
        double[] x = rad_para_sp(200,20000);
        System.out.println(Arrays.toString(x));
    }
}
/*
The MIT License (MIT)

Copyright (c) 2012 Juan Ramón

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
        the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

*/