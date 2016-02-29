package cn.edu.kmust.seanlp.algoritm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;

/**
 * HMM，目前未使用到HMM
 * 
 * @author Zhao Shiyu
 *
 */
public class HMM {
	//staNum, obsNum, staTranMaix, obsTranMaix, initStaPi
	private int staNum;				// 模型中状态的数目
	private int obsNum;				// 每个状态可能输出的观察符号数目
	private double[][] staTranMaix;		// 状态转移概率矩阵
	private double[][] obsTranMaix;		// 观察符号概率矩阵
	private double[] initStaPi;		// 初始状态概率分布
	
	/**
	 * 构造方法
	 * @param sNum
	 * @param oNum
	 */
	public HMM(int sNum, int oNum) {
		super();
		staNum = sNum;
		obsNum = oNum;
		staTranMaix = new double[staNum + 1][staNum + 1];
		obsTranMaix = new double[staNum + 1][obsNum + 1];
		initStaPi = new double[staNum + 1];
	}
	
	/* 前向算法求解观察序列的概率
	 * @param time: 时间
	 * @param obsSer：观察序列
	 * @return obsSerPi：观察序列概率
	 */
	public void forward(int time, int[] obsSer, double obsSerPi) {
		int i, j;   /* 状态索引 */
        int t;      /* 时间索引 */
        double sum = 0.0;
        double[][] alpha = new double[time + 1][staNum + 1];
        
        /* 1. 初始化 */
        for (i = 1; i <= staNum; i++) {
        	alpha[1][i] = initStaPi[i]* obsTranMaix[i][obsSer[1]];
        }
        
        /* 2. 归纳计算 */
        for (t = 1; t < time; t++) {
        	for (j = 1; j <= staNum; j++) {
        		sum = 0.0;
        		for (i = 1; i <= staNum; i++) {
        			sum += alpha[t][i] * staTranMaix[i][j];
        		}
        		alpha[t + 1][j] = sum * obsTranMaix[j][obsSer[t + 1]];
        	}
        }
 
        /* 3. 求和终结 */
        obsSerPi = 0.0;
        for (i = 1; i <= staNum; i++) {
        	obsSerPi += alpha[time][i];
        }
	}
	
	/* 后向算法求解观察序列的概率
	 * @param time: 时间
	 * @param obsSer：观察序列
	 * @return obsSerPi：观察序列概率
	 */
	public void backward(int time, int[] obsSer, double obsSerPi) {
		int i, j;   /* 状态索引 */
        int t;      /* 时间索引 */
        double sum;
        double[][] beta = new double[time + 1][staNum + 1];
        
        /* 1. 初始化 */
        for (i = 1; i <= staNum; i++) {
        	beta[time][i] = 1.0;
        }
        
        /* 2. 归纳计算 */
        for (t = time - 1; t >= 1; t--) {
        	for (i = 1; i <= staNum; i++) {
        		sum = 0.0;
        		for (j = 1; j <= staNum; j++) {
        			sum += staTranMaix[i][j] * obsTranMaix[j][obsSer[t + 1]] * beta[t + 1][j];
        		}
        		beta[t][i] = sum;
        	}
        }
 
        /* 3. 求和终结 */
        obsSerPi = 0.0;
        for (i = 1; i <= staNum; i++) {
        	obsSerPi += beta[1][i];
        }
	}
	
	/* Viterbi算法求解最优状态序列
	 * @param time: 时间
	 * @param obsSer：观察序列
	 * @return staSer： 状态序列
	  * @return obsSerPi：观察序列概率
	 */
	public void viterbi(int time, int[] obsSer, int[] staSer, double obsSerPi) {
		int i, j;
		int t;
		int	maxvalind;
		double	maxval, val;
		double[][] delta = new double[time + 1][staNum + 1];
		int[][] psi = new int[time + 1][staNum + 1];

		/* 1. 初始化  */
		for (i = 1; i <= staNum; i++) {
			delta[1][i] = initStaPi[i] * obsTranMaix[i][obsSer[1]];
			psi[1][i] = 0;
		}	

		/* 2. 归纳计算 */
		for (t = 2; t <= time; t++) {
			for (j = 1; j <= staNum; j++) {
				maxval = 0.0;
				maxvalind = 1;	
				for (i = 1; i <= staNum; i++) {
					val = delta[t - 1][i] * staTranMaix[i][j];
					if (val > maxval) {
						maxval = val;	
						maxvalind = i;	
					}
				}
				delta[t][j] = maxval * obsTranMaix[j][obsSer[t]];
				psi[t][j] = maxvalind; 
			}
		}

		/* 3. 终结 */
		staSer[time] = 1;
		obsSerPi = 0.0;
		for (i = 1; i <= staNum; i++) {
			if (delta[time][i] > obsSerPi) {
				obsSerPi = delta[time][i];
				staSer[time] = i;
			}
		}

		/* 4. 路径(状态序列)回溯 */
		for (t = time - 1; t >= 1; t--) {
			staSer[t] = psi[t+1][staSer[t+1]];
		}
	}
	
	/* 从文件中读入HMM数据，即staNum, obsNum, staTranMaix, obsTranMaix, initStaPi
	 * HMM 文件格式:
	 * ---------------------------------------------
	 * staNum= <number of states>
	 * obsNum= <number of symbols>
	 * staTranMaix:
	 * a11 a12 ... a1N
	 * a21 a22 ... a2N
	 * aN1 aN2 ... aNN
	 * obsTranMaix:
	 * b11 b12 ... b1M
	 * b21 b22 ... b2M
	 * .   .   .   .
	 * bN1 bN2 ... bNM
	 * initStaPi:
	 * pi1 pi2 ... piN
	 *
	 * @param file: HMM数据文件
	 * @param charSet: 文件编码格式
	 */
	public void readHMM(String file, String charSet) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charSet));
			String line = null;
			String[] words = null;

			// 读入staNum
			line = br.readLine();
			words = line.split("\t");	
			staNum = Integer.parseInt(words[2]);			
			
			// 读入obsNum
			line = br.readLine();
			words = line.split("\t");
			obsNum = Integer.parseInt(words[2]);	
			
			/* 为staTranMaix, obsTranMaix, initStaPi 分配空间，0地址不用 */
			staTranMaix = new double[staNum + 1][staNum + 1];
			obsTranMaix = new double[staNum + 1][obsNum + 1];
			initStaPi = new double[staNum + 1];
			/* 读入staTranMaix: */
			line = br.readLine();	
			/* 读入状态转移概率矩阵staTranMaix */
			for(int i = 1; i <= staNum; i++) {
				line = br.readLine();
				String[] values = line.split("\t");
				for(int j = 1; j <= staNum; j++) {
					staTranMaix[i][j] = Double.parseDouble(values[j - 1]);
				}
			}
			
			/* 读入obsTranMaix: */
			line = br.readLine();
			/* 读入观察符号概率矩阵obsTranMaix */
			for(int i = 1; i <= staNum; i++) {
				line = br.readLine();
				String[] values = line.split("\t");
				for(int j = 1; j <= obsNum; j++) {
					obsTranMaix[i][j] = Double.parseDouble(values[j - 1]);
				}
			}
			
			/* 读入initStaPi: */
			line = br.readLine();
			/* 读入初始状态概率分布initStaPi */
			line = br.readLine();
			String[] values = line.split("\t");
			for(int i = 1; i <= staNum; i++) {
				initStaPi[i] =Double.parseDouble(values[i - 1]);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 写HMM数据到文件
	 * @param file: HMM输出文件
	 * @param charSet: 编码格式
	 */
	public void writeHMM(String file, String charSet) {
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charSet));
			bw.write("staNum\t=\t" + staNum + "\n");		// 写入N
			bw.write("staNum\t=\t" + obsNum + "\n");		// 写入M
			
			bw.write("staTranMaix\t:\n");
			/* 写入状态转移概率矩阵staTranMaix */
			for(int i = 1; i <= staNum; i++) {
				String line = "";
				for(int j = 1; j < staNum; j++) {
					line += Double.toString(staTranMaix[i][j]) + "\t";
				}
				line += Double.toString(staTranMaix[i][staNum]) + "\n";
				bw.write(line);
			}
			
			bw.write("obsTranMaix\t:\n");
			/* 写入观察符号概率矩阵obsTranMaix */
			for(int i = 1; i <= staNum; i++) {
				String line = "";
				for(int j = 1; j < obsNum; j++) {
					line += Double.toString(obsTranMaix[i][j]) + "\t";
				}
				line += Double.toString(obsTranMaix[i][obsNum]) + "\n";
				bw.write(line);
			}
			
			bw.write("initStaPi\t:\n");
			/* 写入初始状态概率分布 */
			String line = "";
			for(int i = 1; i < staNum; i++) {
				line += Double.toString(initStaPi[i]) + "\t";
			}
			line += Double.toString(initStaPi[staNum]) + "\n";
			bw.write(line);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 从训练文件构建状态转移矩阵staTranMaix和初始概率分布initStaPi
	 * @param file: 训练文件
	 * @param charSet: 编码格式
	 */
	public void buildPiAndMatrixA(String file, String charSet) {
		/**
         * count matrix format:
         *    0   1 2 3 4
         *    ALL B M E S
         * 0B *   * * * *
         * 1M *   * * * *
         * 2E *   * * * *
         * 3S *   * * * *
         */
		long[][] count = new long[4][staNum + 1];
		try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),charSet));
            String line = null;
            String last = null;
            while ((line = br.readLine()) != null) {
            	/* UTF8文件BOM处理 */
            	if(line.length() > 0) {
            		if((int)line.charAt(0) == 65279) {
            			line = line.substring(1);
            		}
            	}
            	if(line.length() == 0) {// 空行
            		continue;
            	}
            	last = null;
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                	/* 标点符号作为单字词处理  */
                    String word = words[i].trim();
                    int length = word.length();
                    
                    if (length < 1)// 词长度为0
                        continue;
                    if (length == 1) {// 词长度为1
                        count[3][0]++;				// 单字次数自增
                        if (last != null) { 		// 不是句首
                            if (last.length() == 1)	// SS模式
                                count[3][4]++;
                            else					// ES模式
                                count[2][4]++;
                        }/* else {// 第一个词
                        	count[4][0]++;
                        	count[4][4]++;			// $S模式
                        }
                        if(i == words.length - 1) {// 最后一个词
                        	count[3][5]++;			// S$模式
                        }*/
                    } else {// 词长度大于1
                        count[2][0]++;				// E自增
                        count[0][0]++;				// B自增
                        if (length > 2) {// 词长度大于2
                            count[1][0] += length - 2;// M增加
                            count[0][2]++;			// BE模式
                            if (length > 3) {// 词长度大于3
                                count[1][2] += length - 3;// MM模式
                            }
                            count[1][3]++;			// ME模式
                        } else {// 词长度等于2
                            count[0][3]++;			// BE模式
                        }
                        
                        if (last != null) {// 非句首
                            if (last.length() == 1) {// 上一个是单字词
                                count[3][1]++;		// SB模式
                            } else {
                                count[2][1]++;		// EB模式
                            }
                        } /*else {// 第一个词
                        	count[4][0]++;
                        	count[4][1]++;			// $B模式
                        }
                        
                        if(i == words.length - 1) {// 最后一个词
                        	count[2][5]++;			// E$模式
                        }*/
                    }
                    last = word;
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        long allWordCount = count[2][0] + count[3][0];
        initStaPi[1] = (double)count[2][0] / allWordCount;
        initStaPi[2] = 0.0;
        initStaPi[3] = 0.0;
        initStaPi[4] = (double)count[3][0] / allWordCount;
        for (int i = 1; i <= staNum; i++)
            for (int j = 1; j <= staNum; j++) {
            	staTranMaix[i][j] = (double)count[i - 1][j]/ count[i - 1][0];
            }
	}
	
	/* 从训练文件构建观察符号矩阵B
	 * @param file: 训练文件
	 * @param charSet: 训练文件编码格式
	 * @param charMapFile: 字符字库文件
	 * @param charMapCharset: 字符字库文件编码格式
	 */
	public void buildMatrixB(String file, String charSet, String charMapFile, String charMapCharset) {
		/**
         * 字符数目 => M
         * count matrix format:
         *     0   1  2  3  N ...  M
         *    ALL C1 C2 C3 CN ... CM
         * 0B  *  *  *  *  *  ... *
         * 1M  *  *  *  *  *  ... *
         * 2E  *  *  *  *  *  ... *
         * 3S  *  *  *  *  *  ... *
         */
        long[][] count = new long[staNum][obsNum + 1];
        for (int row = 0; row < count.length; row++) {
        	// 加一平滑
            Arrays.fill(count[row], 1);
            count[row][0] = obsNum;
        }
        
        HashMap<Character, Integer> dict = new HashMap<Character, Integer>();
        /* 标点符号应作为single word读入字典  */
        readDict(charMapFile, charMapCharset, dict);
        
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),charSet));
            String line = null;
            while ((line = br.readLine()) != null) {
            	/* UTF8文件BOM处理 */
            	if(line.length() > 0) {
            		if((int)line.charAt(0) == 65279) {
            			line = line.substring(1);
            		}
            	}
            	if(line.length() == 0) {// 空行
            		continue;
            	}
                String[] words = line.split(" ");
                for (int i = 0; i < words.length; i++) {
                	String word = words[i];
                	int length = word.length();
                	
                    if (length < 1)
                        continue;
                    if (length == 1) {// 词长度为1
                    	/* 如果字库中没有这个字，忽略不处理 */
                    	if(!dict.containsKey(word.charAt(0)))
                    		continue;
                        int index = dict.get(word.charAt(0));
                        count[3][0]++;
                        count[3][index]++;
                    } else {// 词长度大于1
                        for (int j = 0; j < length; j++) {
                        	// 获取词中的每个字
                        	/* 如果字库中没有这个字，忽略不处理 */
                        	if(!dict.containsKey(word.charAt(j)))
                        		continue;
                            int index = dict.get(word.charAt(j));
                            if (j == 0) {// 词的第一个字
                                count[0][0]++;
                                count[0][index]++;
                            } else if (j == length-1) {// 词的最后一个字
                                count[2][0]++;
                                count[2][index]++;
                            } else {// 词中间的字
                                count[1][0]++;
                                count[1][index]++;
                            }
                        }
                    } 
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 1; i < staNum + 1; i++) {
            for (int j = 1; j < obsNum + 1; j++) {
                obsTranMaix[i][j] = (double) count[i - 1][j] / count[i - 1][0];
            }
        }
	}
	
	/* 从文件中读入字符构建字符哈希表
	 * @param file: 字典，包括标点符号
	 * @param charSet: 文件编码
	 * @return dict：字哈希表
	 */
	public void readDict(String file, String charSet, HashMap<Character, Integer> dict) {
		try {
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(file),charSet));
            String line = null;
            while ((line = br.readLine()) != null) {
            	/* UTF8文件BOM处理 */
            	if(line.length() > 0) {
            		if((int)line.charAt(0) == 65279) {
            			line = line.substring(1);
            		}
            	}
            	if(line.length() == 0) {// 空行
            		continue;
            	}
            	if(!dict.containsKey(line.charAt(0))) {
            		dict.put(line.charAt(0), dict.size() + 1);
            	}
            }
            br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
