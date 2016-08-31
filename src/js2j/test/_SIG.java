package js2j.test;


import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class _SIG {

	public static int _sig(String H) {
				String U="R3"; 
		String m3="round"; 
		String e3="B3"; 
		String D3="v3"; 
		String N3="I3"; 
		String g3="V"; 
		String K3="toLowerCase"; 
		String n3="substr"; 
		String z3="Z"; 
		String d3="C"; 
		String P3="O"; 
		char[] x3={'a','c','e','i','h','m','l','o','n','s','t','.'}; 
		int[] G3={6,7,1,0,10,3,7,8,11,4,7,9,10,8,0,5,2}; 
		char[] M={'a','c','b','e','d','g','m','-','s','o','.','p','3','r','u','t','v','y','n'}; 
		int[][] X={{17,9,14,15,14,2,3,7,6,11,12,10,9,13,5},{11,6,4,1,9,18,16,10,0,11,11,8,11,9,15,10,1,9,6}}; 
		JSONObject A=(JSONObject) JSONValue.parse("{\"a\":870,\"b\":906,\"c\":167,\"d\":119,\"e\":130,\"f\":899,\"g\":248,\"h\":123,\"i\":627,\"j\":706,\"k\":694,\"l\":421,\"m\":214,\"n\":561,\"o\":819,\"p\":925,\"q\":857,\"r\":539,\"s\":898,\"t\":866,\"u\":433,\"v\":299,\"w\":137,\"x\":285,\"y\":613,\"z\":635,\"_\":638,\"&\":639,\"-\":880,\"/\":687,\"=\":721}"); 
		String[] r3={"0","1","2","3","4","5","6","7","8","9"}; 

				double[] L={1.23413,1.51214,1.9141741,1.5123114,1.51214,1.2651}; 
		double F=1; 

		try {
		F=L[B0I_O.b0I_O(1,2)]; 
		String W=GH.gh(),
		S=GS.gs(X[0],M),
		T=GS.gs(X[1],M); 
		if(EW.ew(W,S)||EW.ew(W,T)) {
			F=L[1]; 
		}
		else {
			F=L[B0I_C.b0I_C(5,3)]; 
		}
} 
		catch(Exception e) {}

		double N=3219; ; 
		for(int Y=0; B0I_Z.b0I_Z(Y,H.length()); Y++) {
			String Q=H.substring(Y,Y+1).toLowerCase(); 
			if(FN.fn(r3,Q) > -1) {
			N=N+(B0I_V.b0I_V(Integer.parseInt(Q),121,F)); 
		}
			else {
			if(B0I_I3.b0I_I3(Q,A)) {
			N=N+(B0I_V3.b0I_v3((int)A.get(Q),F)); 
		}
		}
			N=B0I_B3.b0I_B3(N,0.1); 
		}
		N=Math.round(B0I_R3.b0I_R3(N,1000)); 
		return (int)N; 
	}


}