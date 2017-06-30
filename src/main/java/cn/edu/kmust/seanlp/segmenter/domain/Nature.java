package cn.edu.kmust.seanlp.segmenter.domain;

/**
 * 词性(不同语言的词性集不同)
 * 
 * @author Zhao Shiyu
 *
 */
public enum Nature {
	/**
	 * Thai
	 */
	ADVI,
	ADVN,
	ADVP,
	ADVS,
	BEGIN,
	CFQC,
	CLTV,
	CMTR,
	CNIT,
	CVBL,
	DCNM,
	DDAC,
	DDAN,
	DDAQ,
	DDBQ,
	DIAC,
	DIAQ,
	DIBQ,
	DONM,
	EAFF,
	EITT,
	END,
	FIXN,
	FIXV,
	JCMP,
	JCRG,
	JSBR,
	NCMN,
	NCNM,
	/**
	 * Thai and Lao
	 */
	NEG,
	NLBL,
	NONM,
	NPRP,
	NTTL,
	PDMN,
	PNTR,
	PPRS,
	PREL,
	// Thai and Burmese
	PUNC,
	RPRE,
	VACT,
	VATT,
	VSTA,
	XVAE,
	XVAM,
	XVBB,
	XVBM,
	XVMM,
	/**
	 * 未知词性
	 */
	UN,
	
	//-----------------------------------------------------------------------------
	/**
	 * Lao
	 */
	// Lao and Burmese
	ADJ,
	ADP,
	// Lao and Burmese
	ADV,
	CLF,
	CLT,
	CNM,
	COJ,
	DAN,
	DAQ,
	DAV,
	DBQ,
	DMN,
	FIX,
	IAC,
	IAQ,
	IBQ,
	LBL,
	MTR,
	/**
	 * 老挝语和越南语和柬埔寨语 Burmese
	 */
	N,
	NTR,
	ONM,
	PAM,
	PM,
	PRA,
	PRE,
	PRN,
	PRS,
	REL,
	SCO,
	TTL,
	/**
	 * 老挝语和越南语和柬埔寨语 Burmese
	 */
	V,
	
	
	/**
	 * 越南语词性
	 */
	A,
	/**
	 * 越南语和柬埔寨语
	 */
	AB,
	B,
	C,
	CB,
	/**
	 * 越南语和柬埔寨语
	 */
	CC,
	CH,
	E,
	EB,
	I,
	L,
	/**
	 * 越南语和柬埔寨语
	 */
	M,
	MB,
	NB,
	NC,
	NI,
	NP,
	NU,
	NY,
	P,
	PB,
	R,
	T,
	VB,
	VY,
	X,
	XY,
	Y,
	Z,
	
	/**
	 * Khmer
	 */
	AQ,
	AUX,
	AW,
	DAD,
	// Khmer and Burmese
	FW,
	IN,
	JJ,
	JJ0,
	NN,
	NNP,
	ON,
	PAD,
	PRP,
	PTP,
	QAD,
	RB,
	RPN,
	TXW,
	UH,
	VV,
	W,
	
	/**
	 * Burmese
	 */
	ABB,
	//ADJ,
	//ADV,
	AV,
	CONJ,
	//FW,	
	INT,
	//N,
	NUM,
	PART,
	PPM,
	PRON,
	//PUNC,
	SB,
	TN,
	//V,
	
	
	
	//n.名词 v.动词 pron.代词 adj.形容词 adv.副词 num.数词 art.冠词prep.介词 conj.连词 int.感叹词
	
	/**
	 * 形容词
	 */
	adj,
	/**
	 * 副词
	 */
	adv,
	aux,
	avd,
	clas,
	/**
	 * 连词
	 */
	conj,
	det,
	end,
	fixp,
	/**
	 * 感叹词
	 */
	lnt, //int,
	/**
	 * 名词
	 */
	n,
	neg,
	pref,
	/**
	 * 介词
	 */
	prep,
	/**
	 * 代词
	 */
	pron,
	ques,
	unk,
	/**
	 * 动词
	 */
	v,
	/**
	 * 标点符号
	 */
	w,
	
	
}
