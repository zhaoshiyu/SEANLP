SEANLP: Southeast Asia Natural Language Processing
=====


东南亚语言信息处理
------


**SEANLP**目前有以下功能：

* 泰语：
  * TCC（Thai Character Cluster）切分
  * 音节切分
  * 层叠条件随机场分词
  * 单层条件随机场分词
  * 词典极速分词
  * 词典正向最长匹配分词
  * 词典逆向最长匹配分词
  * 词典正向最段匹配分词
  * 词典逆向最段匹配分词
  * 词性标注
  * 句子相似度计算
  * 关键词抽取
  * 自动摘要

* 越南语：
 * 条件随机场分词
 * 词典极速分词
 * 词典正向最长匹配分词
 * 词典逆向最长匹配分词
 * 词典正向最段匹配分词
 * 词典逆向最段匹配分词
 * 词性标注
 * 句子相似度计算
 * 关键词抽取
 * 自动摘要

* 柬埔寨语（高棉语）：
 * KCC切分
 * 条件随机场分词
 * 词典极速分词
 * 词典正向最长匹配分词
 * 词典逆向最长匹配分词
 * 词典正向最段匹配分词
 * 词典逆向最段匹配分词
 * 词性标注
 * 句子相似度计算
 * 关键词抽取
 * 自动摘要

* 老挝语：
 * 词典极速分词
 * 词典正向最长匹配分词
 * 词典逆向最长匹配分词
 * 词典正向最段匹配分词
 * 词典逆向最段匹配分词
 * 词性标注
 * 句子相似度计算
 * 关键词抽取
 * 自动摘要

* 缅甸语：
 * 音节切分
 * 词典极速分词
 * 词典正向最长匹配分词
 * 词典逆向最长匹配分词
 * 词典正向最段匹配分词
 * 词典逆向最段匹配分词
 * 句子相似度计算



## 说明
1. 什么是TCC (Thai Character Cluster)，借用[Wittawat 		  Jitkrittum的另一种TCC切分实现](https://github.com/wittawatj/jtcc.git)中的解释：proposed in Character Cluster Based Thai   Information Retrieval is a group of inseparable Thai characters. This inseparability derives from Thai writing system which is independent of any context. As a result, TCC can be determined by a simple list of rules describing e.g., what characters need to follow/precede other characters.

2. 泰语TCC和高棉语KCC切分使用`规则+正则表达式`实现，效率较低；泰语TCC切分可参考[Wittawat Jitkrittum的另一种实现](https://github.com/wittawatj/jtcc.git)。

3. 泰语单层条件随机场分词模型比层叠条件随机场分词模型大很多，需要很大的内存才能运行（`-Xmx>2G`）。

4. 缅甸语音节切功能中，由于音节词典中存在不同的编码和字库，不同的编码和字库书写顺序存在区别，所用缅甸语音节切分目前基本不能用。

5. 缅甸语没有词性标注功能，所有缅甸语关键词抽取也存在问题。

6. 分词中层叠条件随机场效果最好，最短匹配分词效果最差。

7. 停用词不够全，主要包含了泰语和越南语的停用词。



## 下载

### 方法一、直接下载源码编译

可以自己接[下载本项目源码](https://github.com/zhaoshiyu/SEANLP/archive/master.zip)进行打包
```
wget https://github.com/zhaoshiyu/SEANLP/archive/master.zip
unzip master.zip
cd SEANLP-master
mvn clean package -Dmaven.test.skip=true
```
或者git clone本项目：
```
git clone https://github.com/ZhaoShiyu/SEANLP.git
cd SEANLP
mvn clean package -Dmaven.test.skip=true
```
注意：此处下载的源码中不包含泰语单层条件随机场分词模型，完整模型可下载[SEANLP-Java-1.0.0-Models.jar](http://pan.baidu.com/s/1jGY0MIa)

### 方法二、下载jar
下载[SEANLP-1.0.0.jar](http://pan.baidu.com/s/1hrkaizi) 或者同时下载[SEANLP-Java-1.0.0.jar和SEANLP-Java-1.0.0-Models.jar](http://pan.baidu.com/s/1jGY0MIa)

## 调用
**SEANLP**所有的功能都可以通过工具类`SEANLP`进行调用。调用格式为`SEANLP.语言.功能`。
### 内存要求
单层条件随机场泰语分词模型很大，需要`-Xmx>2G`

### Demo
demo可以参考下列位置：<br>
	1. [分词](SEANLP/src/test/java/cn/edu/kmust/seanlp/demo/SegmentDemo.java)<br>
	2. [句子相似度计算](SEANLP/src/test/java/cn/edu/kmust/seanlp/demo/SimilarityDemo.java)<br>
	3. [关键词抽取和自动摘要](SEANLP/src/test/java/cn/edu/kmust/seanlp/demo/ExtractDemo.java)<br>

#### 1、分词：
```
package cn.edu.kmust.seanlp.demo;

import cn.edu.kmust.seanlp.SEANLP;

/**
 * 分词demo
 * @author Zhao Shiyu
 *
 */
public class SegmentDemo {
	public static void main(String[] args) {
		//泰语分词
		String thText = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		System.out.println(SEANLP.Thai.syllableSegment(thText));
		System.out.println(SEANLP.Thai.dCRFSegment(thText));
		//System.out.println(SEANLP.Thai.gCRFSegment(thText));
		System.out.println(SEANLP.Thai.datSegment(thText));
		System.out.println(SEANLP.Thai.maxSegment(thText));
		System.out.println(SEANLP.Thai.minSegment(thText));
		System.out.println(SEANLP.Thai.reMaxSegment(thText));
		System.out.println(SEANLP.Thai.reMinSegment(thText));
		
		//越南语分词
		String viText = "Hệ thống tín dụng - ngân hàng cũng tăng trưởng khá, ngày càng giữ vai trò quan trọng trong cơ cấu kinh tế Thủ đô.";
		System.out.println(SEANLP.Vietnamese.crfSegment(viText));
		System.out.println(SEANLP.Vietnamese.datSegment(viText));
		System.out.println(SEANLP.Vietnamese.maxSegment(viText));
		System.out.println(SEANLP.Vietnamese.minSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMaxSegment(viText));
		System.out.println(SEANLP.Vietnamese.reMinSegment(viText));
		
		//柬埔寨语（高棉语）分词
		String khText = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		System.out.println(SEANLP.Khmer.crfSegment(khText));
		System.out.println(SEANLP.Khmer.datSegment(khText));
		System.out.println(SEANLP.Khmer.maxSegment(khText));
		System.out.println(SEANLP.Khmer.minSegment(khText));
		System.out.println(SEANLP.Khmer.reMaxSegment(khText));
		System.out.println(SEANLP.Khmer.reMinSegment(khText));
		
		//老挝语分词
		String loText = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		System.out.println(SEANLP.Lao.datSegment(loText));
		System.out.println(SEANLP.Lao.maxSegment(loText));
		System.out.println(SEANLP.Lao.minSegment(loText));
		System.out.println(SEANLP.Lao.reMaxSegment(loText));
		System.out.println(SEANLP.Lao.reMinSegment(loText));
		
		//缅甸语分词
		String buText = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		System.out.println(SEANLP.Burmese.datSegment(buText));
		System.out.println(SEANLP.Burmese.maxSegment(buText));
		System.out.println(SEANLP.Burmese.minSegment(buText));
		System.out.println(SEANLP.Burmese.reMaxSegment(buText));
		System.out.println(SEANLP.Burmese.reMinSegment(buText));
		System.out.println(SEANLP.Burmese.syllableSegment(buText));
	}
}
```

#### 2、句子相似度计算
```
package cn.edu.kmust.seanlp.demo;

import cn.edu.kmust.seanlp.SEANLP;

/**
 * 句子相似度计算demo
 * @author Zhao Shiyu
 *
 */
public class SimilarityDemo {
	public static void main(String[] args) {
		String thText = "ความสัมพันธ์ในทางเศรษฐกิจกับระบบความสัมพันธ์ทางกฎหมาย";
		String viText = "Hệ thống tín dụng - ngân hàng cũng tăng trưởng khá, ngày càng giữ vai trò quan trọng trong cơ cấu kinh tế Thủ đô.";
		String khText = "ធាតុពិតនិងការបន្ដគំរាមកំហែងមកលើអ្នកការពារសិទ្ធិមនុស្សនៅកម្ពុជា។របាយការណ៍នេះផ្អែកលើការស៊ើបអង្កេតតែ";
		String loText = "ທ່ານວິນເຄັນເປັນປະທານບໍລິສັດອຽວເຊີວີເອີແອນ.ວີ.ກຸ່ມບໍລິສັດການພິມຂອງຊາວດັດ.";
		String buText = "ကံဆိုးကံဇာတာကံထိုက်ကံနခိုကံနှိုးဆော်";
		System.out.println(SEANLP.Thai.sentenceSimilarity(thText, thText));
		System.out.println(SEANLP.Vietnamese.sentenceSimilarity(viText, viText));
		System.out.println(SEANLP.Khmer.sentenceSimilarity(khText, khText));
		System.out.println(SEANLP.Lao.sentenceSimilarity(loText, loText));
		System.out.println(SEANLP.Burmese.sentenceSimilarity(buText, buText));
	}
}
```

#### 3、关键词抽取与自动摘要
```
package cn.edu.kmust.seanlp.demo;

import cn.edu.kmust.seanlp.SEANLP;

/**
 * 关键词抽取和自动摘要demo
 * @author Zhao Shiyu
 *
 */
public class ExtractDemo {
	public static void main(String[] args) {
		String thDocument = "ญี่ปุ่นควรระมัดระวังคำพูดและพฤติกรรมเกี่ยวกับปัญหาทะเลจีนใต้ \n"
				+ "สำนักข่าวแห่งประเทศจีนรายงานว่า นายหง เหล่ย โฆษกกระทรวงการต่างประเทศจีนกล่าวเมื่อวันที่ 19 มกราคมว่า ญี่ปุ่นควรจดจำประวัติศาสตร์การรุกรานให้แม่นยำ สำนึกผิดอย่างยิ่ง และระมัดระวังคำพูดและพฤติกรรมเกี่ยวกับปัญหาทะเลจีนใต้ \n"
				+ "นายชินโซ อาเบะ นายกรัฐมนตรีญี่ปุ่นกล่าวเมื่อวันที่ 18 มกราคมว่า ญี่ปุ่นสใส่ใจอย่างยิ่งต่อการที่จีนสร้างเกาะเทียมกลางทะเลจีนใต้ และทดลองบุกเบิกทรัพยากรทั้งน้ำมันและแก๊สธรรมชาติในทะเลจีนตะวันออก เรียกร้องประชาคมโลกแสดงความเห็นเกี่ยวกับเรื่องนี้มากขึ้น \n"
				+ "นายหง เหล่ยกล่าวต่อการนี้ว่า การบุกเบิกน้ำมันและแก๊สธรรมชาติของจีน ล้วนกระทำในน่านน้ำทะเลที่อยู่ภายใต้การควบคุมของจีนเองโดยปราศจากข้อกังขา ทุกสิ่งทุกอย่างอยู่ในกรอบอธิปไตยของจีนเอง อนึ่ง จีนครองอธิปไตยเหนือหมู่เกาะหนานซาและน่านน้ำทะเลโดยรอบอย่างมิอาจโต้แย้งได้";

		String viDocument = "Đại hội lần thứ XII của Đảng họp phiên trù bị"
				+ "NDĐT- Sáng 20-1, Đại hội đại biểu toàn quốc lần thứ XII của Đảng họp phiên trù bị, hoàn tất công tác chuẩn bị cần thiết cho phiên khai mạc sẽ diễn ra vào 8 giờ sáng 21-1."
				+ "Mở đầu phiên họp trù bị, đồng chí Lê Hồng Anh, Ủy viên Bộ Chính trị, Thường trực Ban Bí thư Trung ương Đảng tuyên bố lý do."
				+ "Đồng chí Trương Tấn Sang, Ủy viên Bộ Chính trị, Chủ tịch nước điều khiển phiên họp. Tiếp đó, Chủ tịch nước Trương Tấn Sang xin ý kiến Đại hội thông qua chương trình phiên họp trù bị, thông qua Quy chế làm việc của Đại hội."
				+ "Đại hội đã hoàn thành các phần việc quan trọng gồm: bầu Đoàn Chủ tịch, Đoàn thư ký, Ban thẩm tra tư cách đại biểu, thông qua chương trình làm việc của Đại hội, thông qua Quy chế bầu cử của Đại hội và thông qua Báo cáo thẩm tra tư cách đại biểu."
				+ "Buổi chiều, các đại biểu nghiên cứu tài liệu tại đoàn."
				+ "Ngày mai 21-1, Đại hội đại biểu toàn quốc lần thứ XII của Đảng khai mạc tại Trung tâm Hội nghị quốc gia, Hà Nội. Đại hội tiến hành từ ngày 21 đến 28-1-2016, có nhiệm vụ đánh giá việc thực hiện Nghị quyết Đại hội XI của Đảng và nhìn lại chặng đường 30 năm đổi mới đất nước; thảo luận, thông qua Báo cáo Chính trị của Ban Chấp hành Trung ương khóa XI; các báo cáo: đánh giá kết quả thực hiện nhiệm vụ phát triển kinh tế- xã hội năm năm 2011-2015 và phương hướng nhiệm vụ phát triển kinh tế- xã hội năm năm 2016- 2020; kiểm điểm sự lãnh đạo, chỉ đạo của Ban Chấp hành Trung ương khóa XI; tổng kết thi hành Điều lệ Đảng khóa XI và đề xuất bổ sung, sửa đổi (nếu có); việc thực hiện Nghị quyết T.Ư 4 khóa XI về xây dựng Đảng. Đại hội bầu Ban Chấp hành Trung ương khóa XII. Chủ đề của Đại hội là Tăng cường xây dựng Đảng trong sạch, vững mạnh; phát huy sức mạnh toàn dân tộc và dân chủ xã hội chủ nghĩa; đẩy mạnh toàn diện, đồng bộ công cuộc đổi mới; bảo vệ vững chắc Tổ quốc, giữ vững môi trường hòa bình, ổn định; phấn đấu sớm đưa nước ta cơ bản trở thành nước công nghiệp theo hướng hiện đại."
				+ "Tham dự Đại hội XII có 1510 đại biểu, đại diện cho hơn 4,5 triệu đảng viên, trong đó đại biểu đương nhiên có 197 đồng chí là Ủy viên Trung ương chính thức và dự khuyết khóa XI; 1300 đại biểu được bầu tại các đại hội Đảng bộ trực thuộc Trung ương; 13 đại biểu chỉ định. Công tác chuẩn bị Đại hội đã được Ban Chấp hành Trung ương, trực tiếp là Bộ Chính trị, Ban Bí thư chỉ đạo chặt chẽ, đến nay đã hoàn tất.";
		
		String loDocument = "ປະທານ​ປະ​ເທດ​ຈີນ​ເລີ່​ມຢ້ຽມຢາມ​ຊາ​ອຸ​ດິດອາຣັບບີ \n"
				+ "ເວລາ 13:35 ​ໂມງ​ຂອງ​ວັນ​ທີ 19 ມັງກອນ​ນີ້​ຕາມ​ເວລາ​ທ້ອງ​ຖິ່ນ, ທ່ານ​ ສີ​ຈີ້​ນຜິ​ງ ປະທານ​ປະ​ເທດ​ຈີນ​ໄດ້​ເດີນທາງ​ໄປ​ຮອດ​ສະໜາມ​ບິນ​ສາກົນກະສັດ​ຄາ​ເລັດ​ທີ່​ນະຄອນຫຼວງລີ​ອັດ​ດ້ວຍ​ຍົນ​ພິ​ເສດ ​ເພື່ອ​ຢ້ຽມຢາມ​ຊາ​ອຸ​ດິດອາຣັບບີທາ​ງລັດຖະກິດ. \n"
				+ "ທ່ານ ສີ​ຈິ້ນຜິ​ງ ຊີ້​ອອກ​ວ່າ, ຊາ​ອຸ​ດິດອາຣັບບີ​ແມ່ນ​ປະ​ເທດ​ອາຣັບ​ແລະ​ອິສລາມ​ທີ່​ໃຫຍ່, ​ແລະ​ກໍ​ແມ່ນ​ສະມາ​ຊິກທີ່ສຳຄັນ​ຂອງ​ກຸ່ມ 20 ປະ​ເທດ. ນັບ​ແຕ່​ຈີນ​ກັບ​ຊາ​ອຸ​ດິດອາຣັບບີ​ສ້າງສາ​ຍພົວພັນ​ການ​ທູດ​ນຳ​ກັນ​ເປັນ​ເວລາ 26 ປີ​ມາ​ນີ້, ການ​ພົວພັນ​ລະຫວ່າງ​ສອງ​ຝ່າຍ​ໄດ້​ຮັບ​ການ​ພັດທະນາ​ແບບ​ກ້າວ​ກະ​ໂດດ ​ໂດຍ​ມີ​ຄວາມ​ໄວ້​ເນື້ອ​ເຊື່ອ​ໃຈ​ກັນ​ດ້ານ​ການ​ເມືອງ​ນັບ​ມື້​ນັບ​ເລິກ​ເຊິ່ງ, ການ​ຮ່ວມ​ມື​ໃນ​ທຸກ​ຂົງ​ເຂດ​​ໄດ້​ຮັບ​ໝາກຜົນ​ທີ່​ອຸດົມສົມບູນ ​ອັນໄດ້​ນຳມາ​ເຊິ່ງຄວາມຜາສຸກທີ່ໃຫຍ່ຫຼວງ​ແກ່​ປະຊາຊົນ​ສອງ​ປະ​ເທດ. ​ໃນ​ໄລຍະ​ຢ້ຽມຢາມ​ຄັ້ງນີ້, ຂ້າພະ​ເຈົ້າຈະ​ຮ່ວມ​ກັບ​ສົມ​ເດັດ​ ​ໂມ​ຮາມ​ເມັດ ບິນ ຊາ​​ເລີມານ ​ເພື່ອ​ແລກປ່ຽນ​ຄວາ​ມຄິດ​ເຫັນ​ກ່ຽວ​ກັບ​ການ​ພົວພັນ​ສອງ​ຝ່າຍ​ພ້ອມ​ດ້ວຍ​ບັນຫາ​ສາກົນ​ແລະ​ພາກ​ພື້ນ​ທີ່​ສົນ​ໃຈຮ່ວມ​ກັນ, ​ແລະ​ເພື່ອ​ຊຸກຍູ້​ສາຍ​ພົວພັນ​ມິ​ດຕະພາບ​ແລະ​ການ​ຮ່ວມ​ມື​ລະຫວ່າງ​ຈີນ​-​ຊາ​ອຸ​ດິດອາຣັບບີ​ໃຫ້​ພັດທະນາ​ວ່ອງໄວ​ແລະ​ໃຫຍ່ຫຼວງ​ກວ່າ​ເກົ່າ. ຂ້າພະ​ເຈົ້າ​ເຊື່ອ​ໝັ້ນວ່າ, ການ​ຢ້ຽມຢາມ​ເທື່ອ​ນີ້ ຈະ​​ເຕັມ​ໄປ​ດ້ວຍ​​ໄມຕີ​ຈິດ​ມິດຕະພາບ​ແລະ​ໝາກຜົນ​ທີ່​ອຸດົມສົມບູນ ​ເຊິ່ງຈະ​ຊ່ວຍ​ຊຸກຍູ້​ການ​ຮ່ວມ​ມື​ລະຫວ່າງ​ສອງ​ຝ່າຍ​ໃນ​ທຸກ​ຂົ​ງ​ເຂດ​ຂຶ້ນສູ່​ລະດັບ​ໃໝ່ ທັງ​ຈະ​ມີ​ຜົນ​ດີ​ຕໍ່​ການ​ຍົກ​ລະດັບ​ການ​ຮ່ວມ​ມື​ລະຫວ່າງ​ຈີນ​ກັບປະ​ເທດ​ສະມາຊິກ​ໃນ​ສະພາ​ຮ່ວມມື​ອ່າວເປີ​ເຊຍ​ໃຫ້​ສູງ​ຂຶ້ນ. \n"
				+ "ຫຼັງຈາກ​ສິ້ນສຸດ​ການ​ຢ້ຽມຢາມ​ຊາ​ອຸ​ດິດອາຣັບບີ​ແລ້ວ, ທ່ານ ສີ​ຈິ້ນຜິ​ງ ຍັງ​ຈະ​ເດີນ​ທາງ​ໄປ​ຢ້ຽມຢາມ​​ເອຢິບ​ແລະ​ອີຣານທາງ​ລັດຖະກິດຕື່ມ​ອີກ. ";
		
		String kmDocument = "អប់រំ​ចំណេះ​ទូទៅ \n "
				+ "កំឡុង​ឆ្នាំ​ ២០០៩-២០១៣ ​សកម្មភាព​គោលនយោបាយ​មួយចំនួន​ត្រូវ​បាន​រៀបចំ​ដូចជា​ ផែនការ​គោល​ស្តីពី​ការអភិវឌ្ឍ​មធ្យមសិក្សា​ និង​សៀវភៅ​ប្រតិបត្តិ​សម្រាប់​ដំណើរការ​មជ្ឈមណ្ឌល​ធនធាន​សម្រាប់​មធ្យម​ សិក្សា​ គោលនយោបាយ​ស្តីពី​សាលា​កុមារ​មេត្រី​នៅ​មធ្យម​សិក្សា​ និង​ការ​កែលម្អ​កម្មវិធីសិក្សា​។​ប្រព័ន្ធ​វិក្រឹតការ​គ្រូបង្រៀន​ លើមុខវិជ្ជា​គណិតវិទ្យា​និង​វិទ្យាសាស្ត្រ​ត្រូវ​បាន​រៀបចំ​។ ​កម្មវិធី​បំណិន​ជីវិត​បច្ចេកវិទ្យា​ ព័ត៌មាន​ និង​ទេសចរណ៍​ត្រូវ​បាន​អនុម័ត​ និង​ស្តង់ដារ​បណ្ណាល័យ​នៅ​មធ្យមសិក្សា​កំពុង​រៀបចំ​ជា​សេចក្តី​ព្រាង។ \n"
				+ "ការចូលរៀន ​និង​គុណភាព​នៅ​កម្រិត​នេះ​មាន​ការប្រែប្រួល​តិចតួច​។ ​អត្រា​ត្រួត​ថ្នាក់​បាន​ថយចុះ​ តិចតួច ​ប៉ុន្តែ​អត្រា​បោះបង់​ការសិក្សា​មិនមាន​ប្រែប្រួលទេ​។ ​សិស្ស​ភាគច្រើ​នបាន​ជ្រើសរើស​យក​មុខវិជ្ជា​ វិទ្យាសាស្ត្រ​ពិត​។ ​ទោះយ៉ាងណា​ក៏ដោយ​ គុណភាព​នៅ​កម្រិត​នេះ​មិន​ទាន់​អាច​វាស់វែង​បាន​នៅ​ឡើយ​ ដោយសារ​ពុំទាន់​បាន​ធ្វើ​តេស្ត​វាយ​តម្លៃ​ថ្នាក់​ជាតិ​នៅ​ថ្នាក់ទី ១២​។ ​អាហារូបករណ៍​បាន​ផ្តល់​ជា​រៀងរាល់​ឆ្នាំ​។ សិស្ស​បាន​ទទួល​មេដាយ​លើ​មុខវិជ្ជា​គណិតវិទ្យា​និង​វិទ្យាសាស្ត្រ ​ពី​កម្មវិធី​ប្រកួត​ស៊ីមេ​អូអូឡាំព្យាដ​ និង​កម្មវិធី​ប្រកួត​អន្តរជាតិ​ផ្សេងៗ​ទៀត​។ \n"
				+ "ប្រព័ន្ធ​វាយតម្លៃ​ ថ្នាក់ជាតិ​ត្រូវ​បាន​ដាក់ឱ្យ​អនុវត្ត​និង​មាន​ថវិកា​សម្រាប់​ដំណើរការ​។ ការប្រឡង​ថ្នាក់ជាតិ​នៅ​ថ្នាក់​ទី​ ៩ ​និង​ទី ​១២​ ត្រូវ​បាន​អនុវត្ត​ជា​ទៀងទាត់​។ \n"
				+ "ចំនួន​អនុវិទ្យាល័យ​ និង​វិទ្យាល័យ​បាន​កើនឡើង​។ ​សាលា​មធ្យម​សិក្សា​បឋមភូមិ​ ៥០ ​ភាគរយ​ ​បាន​អភិវឌ្ឍ​ទៅ​ជា​សាលា​មធ្យម​សិក្សា​ទុតិយភូមិ​។ ​មជ្ឈមណ្ឌល​ធនធាន​នៅ​មធ្យម​សិក្សា​ត្រូវ​បាន​ក៏សាង​នៅ​គ្រប់​រាជធានី ​ខេត្ត​។ ​សាលា​មធ្យម​សិក្សា​បឋម​ភូមិ​ចំនួន​ ១៤១ ​ក្នុង​ខេត្ត​ ៨​ មាន​បន្ទប់​កុំព្យូទ័រ​។ ប្រព័ន្ធ​នៃ​ការបណ្តុះបណ្តាល​និង​វិក្រឹតការ​គ្រូបង្រៀន​ ជាពិសេស​គ្រូបង្រៀន​កម្រិត​អប់រំមូលដ្ឋាន​នៅ​មជ្ឈមណ្ឌល​គរុកោសល្យ​ភូមិភាគ​ និង​វិទ្យាស្ថាន​ជាតិ​អប់រំ​កំពុង​ត្រូវ​បាន​ពង្រីក​។ ការងារ​វិក្រឹតការ​លើ​មុខវិជ្ជា​គណិតវិទ្យា​និង​វិទ្យាសាស្ត្រ ​ក៏​កំពុង​ពង្រីក​ផងដែរ​។ ​នាយក​សាលា​មធ្យម​សិក្សា​ទុតិយភូមិ​ទាំងអស់​ និង​នាយក​សាលា​មធ្យម​សិក្សា​បឋម​ភូមិ​មួយ​ចំនួន​បាន​ទទួល​ការ​បំប៉ន​ស្តីពី​ ការគ្រប់គ្រង​និង​ដឹកនាំ​។ ប្រធាន​ក្រុម​បច្ចេកទេស​នៃ​មជ្ឈមណ្ឌល​ធនធាន​នៅ​មធ្យម​សិក្សា​និង​បណ្តាញ​ ទាំងអស់​បាន​ទទួល​ការបំប៉ន​ ស្តីពី​ស្តង់ដារ​កម្មវិធីសិក្សា​។ គោលនយោបាយសាលាកុមារមេត្រីត្រូវបានអនុវត្តនៅសាលាចំនួន ៨២៣ (៥០,៧៤ ភាគរយ​នៃ​សាលា​មធ្យម​សិក្សា​បឋមភូមិ​)។ \n"
				+ "បញ្ហា​ប្រឈម​ពេល​ខាងមុខ​គឺ​ ការបង្កើន​សមធម៌​ក្នុងការ​ចូលរៀន​នៅ​មធ្យមសិក្សា​ តាមរយៈ​បង្កើន​ចំនួន​សាលា​មធ្យម​សិក្សា​បឋម​ភូមិ​ឱ្យបាន​គ្រប់ឃុំ​ សង្កាត់​និងវិទ្យាល័យ​នៅ​គ្រប់ស្រុក​ ខណ្ឌ​។ គុណភាព​របស់​សិស្ស​បញ្ចប់​ថ្នាក់ទី ​១២​ ត្រូវ​លើក​កម្ពស់​ និង​ផ្តល់​នូវ​ចំណេះដឹង​ពាក់ព័ន្ធ​ដទៃទៀត​ សម្រាប់​ការអប់រំ​បច្ចេកទេស​ វិជ្ជាជីវៈ ​និង​ឧត្តមសិក្សា​។ ​សាលា​មធ្យម​សិក្សា​ភាគច្រើន​ ជាពិសេស​នៅ​តំបន់​ជនបទ​ខ្វះខាត​ធាតុ​ចូល​ដែល​មានគុណភាព​ដូចជា​ គ្រូបង្រៀន​តាមមុខវិជ្ជា ​សម្ភារៈ​បង្រៀន​និង​គ្រឿង​បរិក្ខារ​ សៀវភៅ​សិក្សាគោល​ បន្ទប់ពិសោធ​វិទ្យាសាស្ត្រ​ បន្ទប់​កុំព្យូទ័រ​និង​ភាសា​ និង​បរិក្ខារ​បណ្ណាល័យ​។ វិធីសាស្ត្រ​ក្នុងការ​បង្រៀន​ក្នុង​ពេលបច្ចុប្បន្ន​អនុវត្ត​តាមរបៀប​ ជាមេរៀន​ ចម្លង​តាម​ ដកស្រង់​ និង​ការចងចាំ​។​ វិធីសាស្ត្រ​ទាំងនេះ​គួរ​ត្រូវ​បញ្ចូល​នូវ​ការគិត​ និង​ជំនាញវិភាគ​។ ​ស្តង់ដារ​គ្រូបង្រៀន​គួរ​ត្រូវ​ពិនិត្យ​តាមដាន វាយតម្លៃ​ និង​អភិវឌ្ឍ​ជាប្រចាំ​។ \n"
				+ "ការអប់រំ​បច្ចេកទេស​ជា​មុខងារ​ថ្មី​មួយ​ក្នុង​ ក្រសួង​ និង​ទើបតែ​បាន​អនុម័ត​គោលនយោបាយ​ស្តីពី​ ការអប់រំ​បច្ចេកទេស​។ ​ក្នុង​គោលនយោបាយ​នេះ​បាន​លើក​ឡើង​ពី ​ការបង្កើត​វិទ្យាល័យ​ចំណេះ​ទូទៅ​និង​ បច្ចេកទេស​នៅ​គ្រប់​រាជធានី ​ខេត្ត​។ នាពេលបច្ចុប្បន្ន​ សាលា​មធ្យម​សិក្សា​ចំណេះទូទៅ​និង​បច្ចេកទេស​ចំនួនពីរ​កំពុង​ដំណើរការ ​និង​ផ្តល់​នូវ​មុខវិជ្ជា​សំខាន់​បួន​។​ សិស្ស​ដែល​បញ្ចប់​ការសិក្សា​នៅ​សាលា​ទាំងនេះ​ អាច​រកការងារ​បាន​។ \n"
				+ "កម្មវិធី ​អប់រំ​បច្ចេកទេស​ដែល​កំពុង​អនុវត្ត​នាពេលបច្ចុប្បន្ន ​មាន​កង្វះខាត​ទាំង​ក្របខណ្ឌ​គុណភាព​ដ៏រឹងមាំ​ និង​ធាតុ​ចូល​ដែល​មានគុណភាព​។ ប្រព័ន្ធ​ទទួលស្គាល់​និង​ប្រព័ន្ធធានាគុណភាព​សាលារៀន​មិន​ទាន់​បាន​បង្កើត​ ព្រម​ទាំង​ទំនាក់ទំនង​រវាង​ប្រព័ន្ធអប់រំ​និង​ទីផ្សារ​ការងារ​នៅ​មានកម្រិត​ ។ ​ប្រព័ន្ធ​គ្រប់គ្រង រដ្ឋបាល​និង​ហិរញ្ញវត្ថុ​សាលារៀន​មិន​ទាន់​បង្កើត​។ ការងារ​អប់រំ​បច្ចេកទេស​ត្រូវ​ដកបទពិសោធ​ពីប្រទេស​ដទៃ​។ ដៃគូ​អភិវឌ្ឍ​ជាច្រើន​បាន​ចាប់អារម្មណ៍​គាំទ្រ​ដល់​កម្មវិធីនេះ​។";
		
		String buDocument = "ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုုန္းျမင္႔ရထားလမ္း ေဖါက္လုပ္မည္\n"
				+ "တရုတ္နုိင္ငံေတာ္ေကာင္စီဝင္ ဝမ္ယုံသည္ တရုတ္-အင္ဒုိနီးရွား ပူးေပါင္း ေဆာက္လုပ္ေသာ ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုန္းျမင္႔ရထား စတင္ ေဖါက္လုပ္ေရး အလမ္းအနားသုိ႔ တက္ေရာက္ရန္ ၂၀- ရက္ေန႔မွ ၂၂ ရက္ေန႔အထိ အင္ဒုိနီးရွားသုိ႔ ခရီးထြက္သြားမည္ ျဖစ္ေၾကာင္း၊ အင္ဒုိနီးရွား သမၼတ က်ိဳကုိသည္လည္း အခမ္းအနားသုိ႔ တက္ေရာက္မည္ ျဖစ္ေၾကာင္း၊ ယင္းရထားလမ္းသည္ အင္ဒုိနီးရွား၌ ပထမဦးဆုံး ျမန္နွုန္းျမင္႔ရထားလမ္း ျဖစ္ျပီး အင္ဒုိနီးရွား၏ အေျခခံ အေဆာက္အအုံ ေကာင္းမြန္ ေစေရးနွင္႔ အျပန္အလွန္ ဆက္သြယ္မွု အဆင္႔အတန္း တုိးျမွင္႔ေရးအတြက္ အက်ိဳးရွိမည္ ျဖစ္ေၾကာင္း တရုတ္ျပည္သူ႔ေန႔စဥ္သတင္းစာမွ သတင္းအရ သိရပါသည္။"
				+ "ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုုန္းျမင္႔ရထားလမ္းသည္ စုစုေပါင္း အရွည္ ကီလုိမီတာ ၁၅၀ ရွိျပီး တစ္နာရီလွ်င္ အျမန္ဆုံး ကီလုိမီတာ ၃၀၀ ခုတ္ေမာင္းနုိင္မည္ ျဖစ္ကာ ရထားလမ္း ေဖါက္လုပ္ျပီးေနာက္ ဂ်ာကာတာမွ ဘန္ေဒါင္းသုိ႔ ေပါက္ေရာက္ရန္ အခ်ိန္မွာ လက္ရွိ သုံးနာရီမွေန၍ အာနဂတ္ မိနစ္ ၄၀ မေက်ာ္ေအာင္ ျဖစ္သြားမည္ ျဖစ္ေၾကာင္း၊ ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုုန္းျမင္႔ရထားလမ္းသည္ အင္ဒုိနီးရွားနုိင္ငံသာ မက အေရွ႔ေတာင္အာရွေဒသတြင္ ပထမဦးဆုံး ျမန္နွုန္းျမင္႔ရထားလည္း ျဖစ္မည္ ျဖစ္ေၾကာင္း၊ ျမန္နွုန္းျမင္႔ရထားလမ္း ေဖါက္လုပ္ျခင္းသည္ အင္ဒုိနီးရွားနုိင္ငံအတြက္ နွစ္စဥ္ အလုပ္အကုိင္ေနရာ ၄ ေသာင္းေက်ာ္ကုိ အသစ္ ဖန္တီးေပးမည္ ျဖစ္ျပီး ရထားလမ္း တေလွ်ာက္က ေဒသမ်ားတြင္ စီးပြါးေရး တဆင္႔တုိး ဖြံ႔ျဖိဳးသြားမည္ ျဖစ္ေၾကာင္း သိရပါသည္။"
				+ "ဂ်ာကာတာ-ဘန္ေဒါင္း ျမန္နွုုန္းျမင္႔ရထားလမ္းသည္ တရုတ္ျပည္၏ နုိင္ငံျခား၌ ဒီဇုိင္းေရးဆဲြျခင္း၊ ေဆာက္လုပ္ျခင္း၊ လည္ပတ္ျခင္းနွင္႔ စီမံခန္႔ခဲြျခင္း ျဖစ္စဥ္ တစ္ရပ္လုံးတြင္ ပါဝင္ေဆာင္ရြက္ေသာ ပထမဆုံးေသာ ျမန္နွုန္းျမင္႔ရထားလမ္း ျဖစ္ေၾကာင္း၊ တရုတ္နုိင္ငံတြင္ ကမၻာေပၚတြင္ အတုိင္းအတာ အၾကီးမားဆုံး၊ ျမန္နွုန္း အျမင္႔ဆုံး၊ အေခတ္မီဆုံး၊ စီမံခန္႔ခဲြမွု အေတြ႔အၾကံဳ အရင္႔က်က္ဆုံးေသာ ျမန္နွုန္းျမင္႔ရထားလမ္း ကြန္ရက္ ရွိျပီး တရုတ္ျပည္၏ ျမန္နွုန္းျမင္႔ရထားလမ္းမ်ားသည္ နုိင္ငံတကာ စံခ်ိန္စံနွုန္း ကုိက္မီေရးအဖဲြ႔၊ နုိင္ငံတကာ မီးရထားလုပ္ငန္း အဖဲြ႔ခ်ဳပ္တုိ႔၏ နည္းပညာ စံခ်ိန္စံနွုန္းနွင္႔ လည္း ကုိက္ညီေၾကာင္း ျပည္သူ႔ေန႔စဥ္သတင္းစာတြင္ ေရးသားထားသည္။";
		
		//关键词提取
		System.out.println(SEANLP.Thai.extractKeyword(thDocument, 6));
		//自动摘要
		System.out.println(SEANLP.Thai.extractSummary(thDocument, 3));
		
		System.out.println(SEANLP.Lao.extractKeyword(loDocument, 6));
		System.out.println(SEANLP.Lao.extractSummary(loDocument, 3));
		
		System.out.println(SEANLP.Khmer.extractKeyword(kmDocument, 6));
		System.out.println(SEANLP.Khmer.extractSummary(kmDocument, 3));
		
		System.out.println(SEANLP.Vietnamese.extractKeyword(viDocument, 6));
		System.out.println(SEANLP.Vietnamese.extractSummary(viDocument, 3));
		
		System.out.println(SEANLP.Burmese.extractKeyword(buDocument, 6));
		System.out.println(SEANLP.Burmese.extractSummary(buDocument, 3));
	}

}

```

## 版权
- [Apache License Version 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

### 鸣谢
本项目参考和借鉴了优秀开源项目[HanLP](https://github.com/hankcs/HanLP)。在此表示感谢！

感谢昆明理工大学智能信息处理重点实验室各位老师的指导，感谢给我提供帮助的所有人，谢谢！





作者 [@Zhao Shiyu](https://github.com/zhaoshiyu)


