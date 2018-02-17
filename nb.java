import java.util.*;
import java.io.*;
class train{
	public static ArrayList<HashMap> data = new ArrayList<HashMap>();
	public static HashMap<String, Integer> feature_map;
	public static ArrayList<Integer> sumValues;
	public static ArrayList<String> stopwords = new ArrayList<String>();
	public static ArrayList<String> uniqueArray = new ArrayList<String>();
	public static ArrayList<Double> count = new ArrayList<Double>();
	public static double countHashSum=0.0;
	public static String reg = " |,|\\s+|\\-|\\:|\\/|@|\\.|\\(|\\)|\"|\\<|\\>|$|\\$|1|2|3|4|5|6|7|8|9|0|#|\\'|\\'s|ÿ|\\?|\\;|\\]|\\[|\\_|\\*|\\{|\\}|\\+|\\=|\\!|\\^|\\%|\\\\|\\`|\\||&|\\&|\\~";
	public static void readStopWords() throws IOException{
		BufferedReader dataBR = new BufferedReader(new FileReader(new File("stopwords.txt")));
    	String line = "";
    	while ((line = dataBR.readLine()) != null) {
    		stopwords.add(line);
    	}
	}


	public static void readFileName(String path) throws IOException{
		File[] directories = new File(path).listFiles(File::isDirectory);
		for (int i=0;i<directories.length;i++) {
			readSubFileName(path, directories[i].getName());
			data.add(feature_map);
		}
		count.add(countHashSum);
	}

	public static void readSubFileName(String path,String name) throws IOException{
		double countHash = 0.0;
		feature_map = new HashMap<String, Integer>();
		File folder = new File(path+"\\"+name);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
    		if (file.isFile()) {
        		readContent(folder+"\\"+file.getName());
        		countHash++;
        		countHashSum++;
    		}
		}
		count.add(countHash);
	}

	public static void readContent(String path) throws IOException{
		BufferedReader dataBR = new BufferedReader(new FileReader(new File(path)));
		String line = dataBR.readLine();
    	while (line != null) {
    		String[] value = line.split(":");
    		if (value[0].equals("Lines")) {
    			break;
    		}
    		else {
    			line = dataBR.readLine();
    		}
    	}
    	line = dataBR.readLine();
    	while (line != null) {
    		String[] feature = line.split(reg);
    		for (int i=0;i<feature.length;i++) {
    			feature[i]=feature[i].toLowerCase();
    			if (!stopwords.contains(feature[i])) {
	    			if (!feature_map.containsKey(feature[i])) {
	    				feature_map.put(feature[i],1);
	    			}
	    			else{
	    				int value = feature_map.get(feature[i]);
	    				feature_map.put(feature[i], value+1);
	    			}
    			}
    			if (!uniqueArray.contains(feature[i])) {
    				uniqueArray.add(feature[i]);
    			}
    		}
    		line = dataBR.readLine();
    	}
	}

	public static void calculateValues(){
		sumValues = new ArrayList<Integer>();
		for (int i=0;i<data.size();i++) {
			int sum = 0;
			HashMap<String, Integer> temp = new HashMap<String, Integer>();
			temp = data.get(i);
			for (int f: temp.values()) {
				sum+=f;
			}
			sumValues.add(sum);
		}
	}

	public static void printData(){
		System.out.println(data);
		System.out.println(uniqueArray);
	}
}
class test{
	public static ArrayList<String> teststopwords;
	public static ArrayList<HashMap> testdata;
	public static HashMap<String, Integer> testfeature_map;
	public static ArrayList<Integer> testsumValues;
	public static ArrayList<String> testuniqueArray;
	public static ArrayList<Double> testcount;
	public static double total = 0;
    public static double right = 0;
	public static String reg = " |,|\\s+|\\-|\\:|\\/|@|\\.|\\(|\\)|\"|\\<|\\>|$|\\$|1|2|3|4|5|6|7|8|9|0|#|\\'|\\'s|ÿ|\\?|\\;|\\]|\\[|\\_|\\*|\\{|\\}|\\+|\\=|\\!|\\^|\\%|\\\\|\\`|\\||&|\\&|\\~";
	
	public static void readFileName(String path, ArrayList<String> stopwords, ArrayList<HashMap> data, HashMap<String, Integer> feature_map, ArrayList<Integer> sumValues, ArrayList<String> uniqueArray, ArrayList<Double> count) throws IOException{
		teststopwords = stopwords;
		testdata = data;
		testfeature_map = feature_map;
		testsumValues = sumValues;
		testuniqueArray = uniqueArray;
		testcount = count;
		File[] directories = new File(path).listFiles(File::isDirectory);
		for (int i=0;i<directories.length;i++) {
			readSubFileName(path, directories[i].getName(), i);
		}
		double accuracy = (right/total)*100;
		System.out.println("Accuracy: "+accuracy);
	}
	public static void readSubFileName(String path, String name, int classPostion) throws IOException{
		File folder = new File(path+"\\"+name);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
    		if (file.isFile()) {
        		readContent(folder+"\\"+file.getName(), classPostion);
    		}
		}
	}
	public static void readContent(String path, int classPostion) throws IOException{
		ArrayList<Double> probArray = new ArrayList<Double>();
		for (int j=0;j<testdata.size();j++) {
    		probArray.add(j, Math.log(testcount.get(j)/testcount.get(testcount.size()-1)));
    	}
		double proba;
		int n1,d1,d2;
		BufferedReader dataBR = new BufferedReader(new FileReader(new File(path)));
		String line = dataBR.readLine();
    	while (line != null) {
    		String[] value = line.split(":");
    		if (value[0].equals("Lines")) {
    			break;
    		}
    		else {
    			line = dataBR.readLine();
    		}
    	}
    	if (line==null) {
    		return;
    	}
    	line = dataBR.readLine();
    	while (line != null) {
    		String[] feature = line.split(reg);
    		for (int i=0;i<feature.length;i++) {
    			feature[i]=feature[i].toLowerCase();
    			if (!teststopwords.contains(feature[i])) {
    				for (int j=0;j<testdata.size();j++) {
						HashMap<String, Integer> temp = new HashMap<String, Integer>();
						temp = testdata.get(j);
						if (temp.get(feature[i])==null) {
							n1 = 0;
						}
						else{
							n1 = temp.get(feature[i]);
						}
						d1 = testsumValues.get(j);
						d2 = testuniqueArray.size();
						proba = (double)(n1+1)/(d1+d2);
						proba = Math.log(proba);
						probArray.set(j,proba+probArray.get(j));
    				}
    			}
    		}
    		line = dataBR.readLine();
    	}
    	double maxi = Collections.max(probArray);
    	int pos = probArray.indexOf(maxi);
    	// System.out.println(probArray);
    	// System.out.println(classPostion+" , "+ pos);
    	if (classPostion == pos) {
    		right++;
    		total++;
    	}
    	else{
    		total++;
    	}
    	probArray.clear();
   	}
}
class nb{
	public static void main(String[] args) throws IOException{
		train t = new train();
		t.readStopWords();
		// t.readFileName("D:\\Semester_1\\ML\\Assignment_4\\final_train");
		t.readFileName(args[0]);
		// t.printData();
		t.calculateValues();
		test t2 = new test();
		// t2.readFileName("D:\\Semester_1\\ML\\Assignment_4\\final_test", t.stopwords, t.data, t.feature_map, t.sumValues, t.uniqueArray, t.count);
		t2.readFileName(args[1], t.stopwords, t.data, t.feature_map, t.sumValues, t.uniqueArray, t.count);
	}
}	