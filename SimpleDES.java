import java.util.Scanner;

public class SimpleDES {
    static int[] p10=  {3,5,2,7,4,10,1,9,8,6};
    static int[] p8= {6,3,7,4,8,5,10,9};
    static int[] IP= {2,6,3,1,4,8,5,7};
    static int[] IPinverse= {4,1,3,5,7,2,8,6};
    static int[] EP = {4,1,2,3,2,3,4,1};
    static int[] p4 = {2,4,3,1,};
    static int[][] s0= {{1,0,3,2},{3,2,1,0},{0,2,1,3},{3,1,3,2}};
    static int[][] s1= {{0,1,2,3},{2,0,1,3},{3,0,1,0},{2,1,0,3}};
    static int[] key = new int[10];
    static int[] plainText = new int[8];
    static int [] k = new int[10];
    static int [] k1 = new int[8];
    static int [] k2 = new int[8];
    static int [] Left = new int[4];
    static int [] Right = new int[4];
    static int [] AEP = new int[8];
    static int [] sBox = new int[4];
    static int [] cipher = new int[8];


    public static void main(String[] args) {


        Scanner s =new Scanner(System.in);

        System.out.println("Enter 10 bit key : ");
        for(int i =0;i<10;i++){
            key[i]=s.nextInt();
        }


        System.out.println("Enter 8 bit Plain text : ");
        for(int i =0;i<8;i++){
            plainText[i]=s.nextInt();
        }

        k = p10(p10,key);
        k =LS1(k);

        k1 =p8(p8,k);
        k=LS1(k);
        k=LS1(k);
        k2=p8(p8,k);
        System.out.print("K1: ");
        Print(k1);
        System.out.print("K2: ");
        Print(k2);


        cipher = Encryption();
        System.out.print("\nCipher Text : ");
        Print(cipher);
        plainText = Decryption(cipher);
        System.out.print("\nPlain Text : ");
        Print(plainText);

    }


    public static int[] Encryption(){
        plainText = IP(plainText,IP);
//        System.out.print("IP: " );
//        Print(plainText);
        for (int i =0;i<4;i++){
            Left[i] = plainText[i];
            Right[i] = plainText[i+4];
        }

//        System.out.print("Left: " );
//        Print(Left);
//
//        System.out.print("Right: " );
//        Print(Right);

        AEP = EP(Right , EP);
//        System.out.print("AEP: " );
//        Print(AEP);

        AEP =Xor(AEP,k1);
//        System.out.print("AEP: " );
//        Print(AEP);

        sBox = SboxSubstitution(AEP , s0,s1);
//        System.out.print("sBox: " );
//        Print(sBox);
        sBox = P4(sBox,p4);
//        System.out.print("sBox P4: " );
//        Print(sBox);
        Left = Xor(sBox , Left);
//        System.out.print("Left: " );
//        Print(Left);
        int [] temp = new int[4];
        temp = Left;
        Left = Right;
        Right =temp;

//        Print(Left);
//        Print(Right);

        AEP = EP(Right,EP);
        AEP = Xor(AEP,k2);
//        System.out.print("AEP: " );
//        Print(AEP);
        sBox = SboxSubstitution(AEP , s0,s1);
        sBox = P4(sBox,p4);
        Left = Xor(sBox , Left);
//        System.out.print("Left: " );
//        Print(Left);
        int[] cipher = new int[8];
        for (int i= 0;i<4;i++){
            cipher[i]=Left[i];
            cipher[i+4] = Right[i];
        }
        cipher = IP(cipher,IPinverse);
        return cipher;
    }

    public static int[] Decryption(int[] cipher){

        plainText=IP(cipher,IP);
//        System.out.print("IP: " );
//        Print(plainText);
        for (int i =0;i<4;i++){
            Left[i] = plainText[i];
            Right[i] = plainText[i+4];
        }

        AEP = EP(Right , EP);
        AEP=Xor(AEP,k2);
//        System.out.print("AEP: " );
//        Print(AEP);
        sBox = SboxSubstitution(AEP , s0,s1);
        sBox = P4(sBox,p4);
//        System.out.print("sbox p4: " );
//        Print(sBox);
        Left = Xor(sBox , Left);
//        System.out.print("Left: " );
//        Print(Left);
        int[] temp = new int[4];
        temp = Left;
        Left = Right;
        Right =temp;

        AEP = EP(Right,EP);
        AEP = Xor(AEP,k1);
//        System.out.print("AEP: " );
//        Print(AEP);
        sBox = SboxSubstitution(AEP , s0,s1);
        sBox = P4(sBox,p4);

        Left = Xor(sBox , Left);

        for(int i=0;i<4;i++) {
            plainText[i]=Left[i];
            plainText[i+4]=Right[i];
        }

        plainText = IP(plainText,IPinverse);

        return plainText;
    }
    public static void Print(int[] a){
        for (int i=0;i<a.length;i++){
            System.out.print(a[i]);
        }
        System.out.println();
    }
    public static int[] P4 (int[] s , int[] p4){
        int[] s1 = new int[4];
        for(int i =0;i<4;i++){
            s1[i]=s[p4[i]-1];
        }
        return s1;
    }
    public static int[] SboxSubstitution( int[] a , int[][]s0 ,int[][] s1){
        int[] sBox= new int[4];
        int r1 = ToDecimal(a[0] , a[3]);
        int c1 = ToDecimal(a[1],a[2]);
        int r2 = ToDecimal(a[4] , a[7]);
        int c2 = ToDecimal(a[5],a[6]);
//        System.out.println(r1 +"\t"+ c1);
//        System.out.println(r2 +"\t"+ c2);

        int a1 = s0[r1][c1];
        int b1 = s1[r2][c2];
//        System.out.println(a1+"\t"+b1);
        a1 = ToBinary(a1);
        b1 = ToBinary(b1);
//        System.out.println(a1 +"\t"+ b1);

        if(a1==1 && b1!=1){
            sBox[0]=0;
            sBox[1]=1;
            sBox[3]=b1%10;
            sBox[2]=b1/10;
        }
        if(b1==1 && a1!=1){
            sBox[2]=0;
            sBox[3]=1;
            sBox[1]=a1%10;
            sBox[0]=a1/10;
        }
        else {
            sBox[1]=a1%10;
            sBox[0]=a1/10;
            sBox[3]=b1%10;
            sBox[2]=b1/10;

        }
        return sBox;




    }
    public static int ToBinary(int a){
        if(a ==0) return 00;
        else if(a==1) return 01;
        else if(a==2) return 10;
        else return 11;
    }
    public static int ToDecimal(int a,int b){
        if(a==0 & b==0) return 0;
        else if(a==0 & b==1) return 1;
        else if(a==1 & b==0) return 2;
        else return 3;
    }
    public static int[]Xor(int[]a , int[] b){
        int[] xor = new int[a.length];
        for(int i=0;i<a.length;i++){
            xor[i] = a[i]^b[i];
        }
        return xor;

    }
    public static int[] EP(int[] right , int[] EP){
        int[] AEP = new int[8];
        for (int i =0;i<8;i++){
            AEP[i] = right[EP[i]-1];
        }
        return AEP;
    }
    public static int[] IP(int[] plainText , int[] IP){
        int[] P = new int[8];

        for(int i =0;i<8;i++){
            P[i] = plainText[IP[i]-1];
        }
        return P;
    }

    public static int[] p10(int[]p10, int[] key){

        int[] k1 = new int[10];
        for(int i =0;i<10;i++){
//            System.out.println(key.charAt(p10[i]-1));
            k1[i]=key[p10[i]-1];
        }
        return k1;

    }
    public static int[] LS1 (int[] key){
        int x = key[0];
        int y = key[5];
        for(int i =0;i<4;i++){
            key[i] = key[i+1];
            key[i+5] = key[i+6];
        }
        key[4]=x;
        key[9]=y;

        return key;
    }


    public static int[] p8(int[]p8 , int[]k){
        int [] k1 = new int[8];
        for(int i =0;i<8;i++){
            k1[i] = k[p8[i]-1];
        }
        return k1;
    }
}
