package cn.edu.kmust.seanlp.collection.trie;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.zip.GZIPOutputStream;

import cn.edu.kmust.seanlp.Config.Log;
import cn.edu.kmust.seanlp.util.ByteArray;

/**
 * 双数组Trie<br>
 * 原始版本是KOMIYA Atsushi对Taku Kudo的 C++ 版 Double Array Trie的实现，https://github.com/komiya-atsushi/darts-java.git  <br>
 * hankcs实现了一个Searcher结构<br>
 * 这里基本没有做什么修改，只是加了几个接口实现
 * @author Zhao Shiyu
 *
 * @param <V>
 */
public class DATrie<V> implements ITrie {
	
	private final static int UNIT_SIZE = 8;
	
	private static class Node {
		int code;
		int depth;
		int left;
		int right;

		@Override
		public String toString() {
			return "Node{" + "code=" + code + ", depth=" + depth + ", left="
					+ left + ", right=" + right + '}';
		}
	}
	
	protected int check[];
	protected int base[];

	private boolean used[];
	/**
	 * base 和 check 的大小
	 */
	protected int size;
	private int allocSize;
	private List<String> key;
	private int keySize;
	private int length[];
	private int value[];
	protected V[] v;
	private int progress;
	private int nextCheckPos;
	private int maxLength;
	int error;
	
	/**
	 * 构造函数
	 */
	public DATrie() {
		check = null;
		base = null;
		used = null;
		size = 0;
		allocSize = 0;
		error = 0;
	}
	
	public int getUnitSize() {
		return UNIT_SIZE;
	}

	public int getSize() {
		return size;
	}

	public int getTotalSize() {
		return size * UNIT_SIZE;
	}

	public int getNonzeroSize() {
		int result = 0;
		for (int i = 0; i < check.length; ++i)
			if (check[i] != 0)
				++result;
		return result;
	}
	
	/**
	 * 树叶子节点个数
	 *
	 * @return
	 */
	public int size() {
		return v.length;
	}

	/**
	 * 获取check数组引用，不要修改check
	 *
	 * @return
	 */
	public int[] getCheck() {
		return check;
	}

	/**
	 * 获取base数组引用，不要修改base
	 *
	 * @return
	 */
	public int[] getBase() {
		return base;
	}
	
	public int build(List<String> keys) {
		assert keys.size() > 0 : "键值个数为0！";
		return build(keys, null, null, keys.size());
	}
	
	public int build(List<String> keys, List<V> values) {
		assert keys.size() == values.size() : "键的个数与值的个数不一样！";
		assert keys.size() > 0 : "键值个数为0！";
		v = (V[]) values.toArray();
		return build(keys, null, null, keys.size());
	}

	public int build(List<String> keys, V[] values) {
		assert keys.size() == value.length : "键的个数与值的个数不一样！";
		assert keys.size() > 0 : "键值个数为0！";
		v = values;
		return build(keys, null, null, keys.size());
	}

	/**
	 * 构建DAT
	 *
	 * @param entrySet 注意此entrySet一定要是字典序的！否则会失败
	 * @return
	 */
	public int build(Set<Map.Entry<String, V>> entrySet) {
		List<String> keyList = new ArrayList<String>(entrySet.size());
		List<V> valueList = new ArrayList<V>(entrySet.size());
		for (Map.Entry<String, V> entry : entrySet) {
			keyList.add(entry.getKey());
			valueList.add(entry.getValue());
		}

		return build(keyList, valueList);
	}

	/**
	 * 方便地构造一个双数组trie树
	 *
	 * @param keyValueMap 升序键值对map
	 * @return 构造结果
	 */
	public int build(TreeMap<String, V> keyValueMap) {
		assert keyValueMap != null;
		Set<Map.Entry<String, V>> entrySet = keyValueMap.entrySet();
		return build(entrySet);
	}

	/**
	 * 唯一的构建方法
	 *
	 * @param _key 值set，必须字典序
	 * @param _length 对应每个key的长度，留空动态获取
	 * @param _value 每个key对应的值，留空使用key的下标作为值
	 * @param _keySize key的长度，应该设为_key.size
	 * @return 是否出错
	 */
	public int build(List<String> _key, int _length[], int _value[], int _keySize) {
		if (_keySize > _key.size() || _key == null)
			return 0;

		// progress_func_ = progress_func;
		key = _key;
		length = _length;
		keySize = _keySize;
		value = _value;
		progress = 0;

		resize(65536 * 32); // 32个双字节

		base[0] = 1;
		nextCheckPos = 0;

		Node root_node = new Node();
		root_node.left = 0;
		root_node.right = keySize;
		root_node.depth = 0;

		List<Node> siblings = new ArrayList<Node>();
		fetch(root_node, siblings);
		insert(siblings);

		// size += (1 << 8 * 2) + 1; // ???
		// if (size >= allocSize) resize (size);

		used = null;
		key = null;
		length = null;

		return error;
	}
	
	/**
	 * 拓展数组
	 *
	 * @param newSize 新数组大小
	 * @return
	 */
	private int resize(int newSize) {
		int[] newBbase = new int[newSize];
		int[] newCheck = new int[newSize];
		boolean[] newUsed = new boolean[newSize];
		if (allocSize > 0) {
			System.arraycopy(base, 0, newBbase, 0, allocSize);
			System.arraycopy(check, 0, newCheck, 0, allocSize);
			System.arraycopy(used, 0, newUsed, 0, allocSize);
		}
		base = newBbase;
		check = newCheck;
		used = newUsed;
		return allocSize = newSize;
	}
	
	/**
	 * 获取直接相连的子节点
	 *
	 * @param parent 父节点
	 * @param siblings （子）兄弟节点
	 * @return 兄弟节点个数
	 */
	private int fetch(Node parent, List<Node> siblings) {
		if (error < 0) {
			return 0;
		}
		int prev = 0;
		for (int i = parent.left; i < parent.right; i++) {
			if ((length != null ? length[i] : key.get(i).length()) < parent.depth) {
				continue;
			}
			String tmp = key.get(i);
			int cur = 0;
			if ((length != null ? length[i] : tmp.length()) != parent.depth) {
				cur = (int) tmp.charAt(parent.depth) + 1;
			}
			if (prev > cur) {
				error = -3;
				return 0;
			}
			if (cur != prev || siblings.size() == 0) {
				Node tmp_node = new Node();
				tmp_node.depth = parent.depth + 1;
				tmp_node.code = cur;
				tmp_node.left = i;
				if (siblings.size() != 0) {
					siblings.get(siblings.size() - 1).right = i;
				}
				//System.out.println(tmp_node);
				siblings.add(tmp_node);
			}
			prev = cur;
		}
		if (siblings.size() != 0) {
			siblings.get(siblings.size() - 1).right = parent.right;
		}
		return siblings.size();
	}
	
	/**
	 * 插入节点
	 *
	 * @param siblings 等待插入的兄弟节点
	 * @return 插入位置
	 */
	private int insert(List<Node> siblings) {
		if (error < 0) 
			return 0;
		
		int begin = 0;
		int pos = Math.max(siblings.get(0).code + 1, nextCheckPos) - 1;
		int nonzero_num = 0;
		int first = 0;
		if (allocSize <= pos) {
			resize(pos + 1);
		}
		outer: // 此循环体的目标是找出满足base[begin + a1...an] == 0的n个空闲空间,a1...an是siblings中的n个节点
		while (true) {
			pos++;
			if (allocSize <= pos) {
				resize(pos + 1);
			}
			if (check[pos] != 0) {
				nonzero_num++;
				continue;
			} else if (first == 0) {
				nextCheckPos = pos;
				first = 1;
			}
			begin = pos - siblings.get(0).code; // 当前位置离第一个兄弟节点的距离
			if (allocSize <= (begin + siblings.get(siblings.size() - 1).code)) {
				// progress can be zero // 防止progress产生除零错误
				double l = (1.05 > 1.0 * keySize / (progress + 1)) ? 1.05 : 1.0 * keySize / (progress + 1);
				resize((int) (allocSize * l));
			}

			if (used[begin]) {
				continue;
			}

			for (int i = 1; i < siblings.size(); i++) {
				if (check[begin + siblings.get(i).code] != 0) {
					continue outer;
				}
			}
			
			break;
		}

		// -- Simple heuristics --
		// if the percentage of non-empty contents in check between the
		// index
		// 'next_check_pos' and 'check' is greater than some constant value
		// (e.g. 0.9),
		// new 'next_check_pos' index is written by 'check'.
		if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95) {
			nextCheckPos = pos; // 从位置 next_check_pos 开始到 pos间，如果已占用的空间在95%以上，下次插入节点时，直接从 pos 位置处开始查找
		}
		
		used[begin] = true;
		size = (size > begin + siblings.get(siblings.size() - 1).code + 1) ? size : begin + siblings.get(siblings.size() - 1).code + 1;

		for (int i = 0; i < siblings.size(); i++) {
			check[begin + siblings.get(i).code] = begin;
			// System.out.println(this);
		}

		for (int i = 0; i < siblings.size(); i++) {
			List<Node> new_siblings = new ArrayList<Node>();

			if (fetch(siblings.get(i), new_siblings) == 0) {// 一个词的终止且不为其他词的前缀
				base[begin + siblings.get(i).code] = (value != null) ? (-value[siblings.get(i).left] - 1) : (-siblings.get(i).left - 1);
				// System.out.println(this);
				if (value != null && (-value[siblings.get(i).left] - 1) >= 0) {
					error = -2;
					return 0;
				}

				progress++;
				// if (progress_func_) (*progress_func_) (progress,
				// keySize);
			} else {
				int h = insert(new_siblings); // dfs
				base[begin + siblings.get(i).code] = h;
				// System.out.println(this);
			}
		}
		return begin;
	}
	
	/**
	 * 插入节点
	 *
	 * @param siblings 等待插入的兄弟节点
	 * @return 插入位置
	 */
	public int addAll(List<Node> siblings) {
		if (error < 0)
			return 0;

		int begin = 0;
		int pos = Math.max(siblings.get(0).code + 1, nextCheckPos) - 1;
		int nonzero_num = 0;
		int first = 0;

		if (allocSize <= pos) {
			resize(pos + 1);
		}
		
		outer: // 此循环体的目标是找出满足base[begin + a1...an] == 0的n个空闲空间,a1...an是siblings中的n个节点
		while (true) {
			pos++;

			if (allocSize <= pos) {
				resize(pos + 1);
			}
			
			if (check[pos] != 0) {
				nonzero_num++;
				continue;
			} else if (first == 0) {
				nextCheckPos = pos;
				first = 1;
			}

			begin = pos - siblings.get(0).code; // 当前位置离第一个兄弟节点的距离
			if (allocSize <= (begin + siblings.get(siblings.size() - 1).code)) {
				// progress can be zero // 防止progress产生除零错误
				double l = (1.05 > 1.0 * keySize / (progress + 1)) ? 1.05 : 1.0 * keySize / (progress + 1);
				resize((int) (allocSize * l));
			}

			if (used[begin]) {
				continue;
			}
			
			for (int i = 1; i < siblings.size(); i++) {
				if (check[begin + siblings.get(i).code] != 0) {
					continue outer;
				}
			}
			
			break;
		}

		// -- Simple heuristics --
		// if the percentage of non-empty contents in check between the
		// index
		// 'next_check_pos' and 'check' is greater than some constant value
		// (e.g. 0.9),
		// new 'next_check_pos' index is written by 'check'.
		if (1.0 * nonzero_num / (pos - nextCheckPos + 1) >= 0.95) {
			nextCheckPos = pos; // 从位置 next_check_pos 开始到 pos间，如果已占用的空间在95%以上，下次插入节点时，直接从 pos 位置处开始查找
		}
		
		used[begin] = true;
		size = (size > begin + siblings.get(siblings.size() - 1).code + 1) ? size : begin + siblings.get(siblings.size() - 1).code + 1;

		for (int i = 0; i < siblings.size(); i++) {
			check[begin + siblings.get(i).code] = begin;
			// System.out.println(this);
		}

		for (int i = 0; i < siblings.size(); i++) {
			List<Node> new_siblings = new ArrayList<Node>();

			if (fetch(siblings.get(i), new_siblings) == 0) { // 一个词的终止且不为其他词的前缀
				base[begin + siblings.get(i).code] = (value != null) ? (-value[siblings.get(i).left] - 1) : (-siblings.get(i).left - 1);
				// System.out.println(this);

				if (value != null && (-value[siblings.get(i).left] - 1) >= 0) {
					error = -2;
					return 0;
				}

				progress++;
				// if (progress_func_) (*progress_func_) (progress,
				// keySize);
			} else {
				int h = insert(new_siblings); // dfs
				base[begin + siblings.get(i).code] = h;
				// System.out.println(this);
			}
		}
		return begin;
	}
	
	/**
	 * 精确匹配
	 *
	 * @param key
	 *            键
	 * @return 值
	 */
	public int exactMatchSearch(String key) {
		return exactMatchSearch(key, 0, 0, 0);
	}

	public int exactMatchSearch(String key, int pos, int len, int nodePos) {
		if (len <= 0)
			len = key.length();
		if (nodePos <= 0)
			nodePos = 0;

		int result = -1;

		char[] keyChars = key.toCharArray();

		int b = base[nodePos];
		int p;

		for (int i = pos; i < len; i++) {
			p = b + (int) (keyChars[i]) + 1;
			if (b == check[p])
				b = base[p];
			else
				return result;
		}

		p = b;
		int n = base[p];
		if (b == check[p] && n < 0) {
			result = -n - 1;
		}
		return result;
	}

	/**
	 * 精确查询
	 *
	 * @param keyChars 键的char数组
	 * @param pos char数组的起始位置
	 * @param len 键的长度
	 * @param nodePos 开始查找的位置（本参数允许从非根节点查询）
	 * @return 查到的节点代表的value ID，负数表示不存在
	 */
	public int exactMatchSearch(char[] keyChars, int pos, int len, int nodePos) {
		int result = -1;
		int b = base[nodePos];
		int p;

		for (int i = pos; i < len; i++) {
			p = b + (int) (keyChars[i]) + 1;
			if (b == check[p]) {
				b = base[p];
			} else {
				return result;
			}
		}
		
		p = b;
		int n = base[p];
		if (b == check[p] && n < 0) {
			result = -n - 1;
		}
		return result;
	}
	
	public List<Integer> commonPrefixSearch(String key) {
		return commonPrefixSearch(key, 0, 0, 0);
	}

	/**
	 * 前缀查询
	 *
	 * @param key 查询字串
	 * @param pos 字串的开始位置
	 * @param len 字串长度
	 * @param nodePos base中的开始位置
	 * @return 一个含有所有下标的list
	 */
	public List<Integer> commonPrefixSearch(String key, int pos, int len, int nodePos) {
		if (len <= 0) {
			len = key.length();
		}
		
		if (nodePos <= 0) {
			nodePos = 0;
		}
		
		List<Integer> result = new ArrayList<Integer>();
		char[] keyChars = key.toCharArray();
		int b = base[nodePos];
		int n;
		int p;

		for (int i = pos; i < len; i++) {
			p = b + (int) (keyChars[i]) + 1; // 状态转移 p = base[char[i-1]] + char[i] + 1
			if (b == check[p]) { // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
				b = base[p];
			} else {
				return result;
			}
			
			p = b;
			n = base[p];
			if (b == check[p] && n < 0) {  // base[p] == check[p] && base[p] < 0		
				result.add(-n - 1);  // 查到一个词
			}
		}
		
		return result;
	}

	/**
	 * 前缀查询，包含值
	 *
	 * @param key 键
	 * @return 键值对列表
	 * @deprecated 最好用优化版的
	 */
	public LinkedList<Map.Entry<String, V>> commonPrefixSearchWithValue(String key) {
		int len = key.length();
		LinkedList<Map.Entry<String, V>> result = new LinkedList<Map.Entry<String, V>>();
		char[] keyChars = key.toCharArray();
		int b = base[0];
		int n;
		int p;

		for (int i = 0; i < len; ++i) {
			p = b;
			n = base[p];
			if (b == check[p] && n < 0) {  // base[p] == check[p] && base[p] < 0
				result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, 0, i), v[-n - 1])); // 查到一个词
			}

			p = b + (int) (keyChars[i]) + 1; // 状态转移 p = base[char[i-1]] + char[i] + 1
			// 下面这句可能产生下标越界，不如改为if (p < size && b == check[p])，或者多分配一些内存
			if (b == check[p]) { // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
				b = base[p];
			} else {
				return result;
			}
		}

		p = b;
		n = base[p];
		if (b == check[p] && n < 0) {
			result.add(new AbstractMap.SimpleEntry<String, V>(key, v[-n - 1]));
		}
		
		return result;
	}

	/**
	 * 优化的前缀查询，可以复用字符数组
	 *
	 * @param keyChars
	 * @param begin
	 * @return
	 */
	public LinkedList<Map.Entry<String, V>> commonPrefixSearchWithValue(
			char[] keyChars, int begin) {
		int len = keyChars.length;
		LinkedList<Map.Entry<String, V>> result = new LinkedList<Map.Entry<String, V>>();
		int b = base[0];
		int n;
		int p;

		for (int i = begin; i < len; ++i) {
			p = b;
			n = base[p];
			if (b == check[p] && n < 0) {  // base[p] == check[p] && base[p] < 0
				result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, begin, i - begin), v[-n - 1])); // 查到一个词
			}

			p = b + (int) (keyChars[i]) + 1; // 状态转移 p = base[char[i-1]] + char[i] + 1
			// 下面这句可能产生下标越界，不如改为if (p < size && b == check[p])，或者多分配一些内存
			if (b == check[p]) { // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
				b = base[p];
			} else {
				return result;
			}
		}

		p = b;
		n = base[p];
		if (b == check[p] && n < 0) {
			result.add(new AbstractMap.SimpleEntry<String, V>(new String(keyChars, begin, len - begin), v[-n - 1]));
		}

		return result;
	}
	
	/**
	 * 从值数组中提取下标为index的值<br>
	 * 注意为了效率，此处不进行参数校验
	 *
	 * @param index  下标
	 * @return 值
	 */
	public V getValue(int index) {
		return v[index];
	}
	
	/**
	 * 精确查询
	 *
	 * @param key 键
	 * @return 值
	 */
	public V get(String key) {
		int index = exactMatchSearch(key);
		if (index >= 0) {
			return getValue(index);
		}
		return null;
	}

	/**
	 * 精确查询
	 * @param key
	 * @return
	 */
	public V get(char[] key) {
		int index = exactMatchSearch(key, 0, key.length, 0);
		if (index >= 0) {
			return getValue(index);
		}
		return null;
	}
	
	public V[] getValueArray(V[] a) {
		int size = v.length;
		if (a.length < size) {
			a = (V[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
		}
		System.arraycopy(v, 0, a, 0, size);
		return a;
	}
	
	/**
	 * 沿着路径转移状态
	 *
	 * @param path
	 * @return
	 */
	protected int transition(String path) {
		return transition(path.toCharArray());
	}

	/**
	 * 沿着节点转移状态
	 *
	 * @param path
	 * @return
	 */
	protected int transition(char[] path) {
		int b = base[0];
		int p;

		for (int i = 0; i < path.length; ++i) {
			p = b + (int) (path[i]) + 1;
			if (b == check[p]) {
				b = base[p];
			} else {
				return -1;
			}
		}
		p = b;
		return p;
	}
	
	/**
	 * 转移状态
	 *
	 * @param current
	 * @param c
	 * @return
	 */
	protected int transition(int current, char c) {
		int b = base[current];
		int p;

		p = b + c + 1;
		if (b == check[p]) {
			b = base[p];
		} else {
			return -1;
		}
		
		p = b;
		return p;
	}

	/**
	 * 沿着路径转移状态
	 *
	 * @param path 路径
	 * @param from 起点（根起点为base[0]=1）
	 * @return 转移后的状态（双数组下标）
	 */
	public int transition(String path, int from) {
		int b = from;
		int p;

		for (int i = 0; i < path.length(); ++i) {
			p = b + (int) (path.charAt(i)) + 1;
			if (b == check[p]) {
				b = base[p];
			} else {
				return -1;
			}
		}
		p = b;
		return p;
	}

	/**
	 * 转移状态
	 * 
	 * @param c
	 * @param from
	 * @return
	 */
	public int transition(char c, int from) {
		int b = from;
		int p;

		p = b + (int) (c) + 1;
		if (b == check[p]) {
			b = base[p];
		} else {
			return -1;
		}
		
		return b;
	}
	
	/**
	 * 检查状态是否对应输出
	 *
	 * @param state 双数组下标
	 * @return 对应的值，null表示不输出
	 */
	public V output(int state) {
		if (state < 0)
			return null;
		int n = base[state];
		if (state == check[state] && n < 0) {
			return v[-n - 1];
		}
		return null;
	}

	/**
	 * 更新某个键对应的值
	 *
	 * @param key   键
	 * @param value   值
	 * @return 是否成功（失败的原因是没有这个键）
	 */
	public boolean set(String key, V value) {
		int index = exactMatchSearch(key);
		if (index >= 0) {
			v[index] = value;
			return true;
		}

		return false;
	}
	
	@Override
	public int getMaxLength() {
		return 0;
	}
	
	public boolean containsKey(String key) {
		return exactMatchSearch(key) >= 0;
	}
	
	@Override
	public boolean contains(char[] text, int offset, int count) {
		return exactMatchSearch(new String(text, offset, count)) >= 0;
	}
	
	@Override
	public boolean contains(String text, int offset, int count) {
		return contains(text.toCharArray(), offset, count);
	}

	@Override
	public boolean contains(String key) {
		return exactMatchSearch(key) >= 0;
	}
	
	public void clear() {
		check = null;
		base = null;
		used = null;
		allocSize = 0;
		size = 0;
	}
	
	@Override
	public String toString() {
		return "DoubleArrayTrie{"
				+"size=" + size
				+ ", allocSize=" + allocSize
				+ ", key=" + key
				+ ", keySize=" + keySize
				+", progress=" + progress
				+ ", nextCheckPos=" + nextCheckPos
				+ ", error =" + error + '}';
	}
	
	public boolean save(String fileName) {
		DataOutputStream out;
		try {
			out = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(fileName)));
			out.writeInt(size);
			for (int i = 0; i < size; i++) {
				out.writeInt(base[i]);
				out.writeInt(check[i]);
			}
			out.close();
		} catch (Exception e) {
			Log.logger.warning("保存失败" + e);
			return false;
		}

		return true;
	}

	/**
	 * 将base和check保存下来
	 *
	 * @param out
	 * @return
	 */
	public boolean save(DataOutputStream out) {
		try {
			out.writeInt(size);
			for (int i = 0; i < size; i++) {
				out.writeInt(base[i]);
				out.writeInt(check[i]);
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	public void save(ObjectOutputStream out) throws IOException {
		out.writeObject(base);
		out.writeObject(check);
	}
	
	public boolean load(ByteArray byteArray, V[] value) {
		if (byteArray == null)
			return false;
		size = byteArray.nextInt();
		base = new int[size + 65535]; // 多留一些，防止越界
		check = new int[size + 65535];
		for (int i = 0; i < size; i++) {
			base[i] = byteArray.nextInt();
			check[i] = byteArray.nextInt();
		}
		v = value;
		return true;
	}
	
	/**
	 * 一个搜索工具（注意，当调用next()返回false后不应该继续调用next()，除非reset状态）
	 */
	public class Searcher {
		/**
		 * key的起点
		 */
		public int begin;
		/**
		 * key的长度
		 */
		public int length;
		/**
		 * key的字典序坐标
		 */
		public int index;
		/**
		 * key对应的value
		 */
		public V value;
		/**
		 * 传入的字符数组
		 */
		private char[] charArray;
		/**
		 * 上一个node位置
		 */
		private int last;
		/**
		 * 上一个字符的下标
		 */
		private int i;
		/**
		 * charArray的长度，效率起见，开个变量
		 */
		private int arrayLength;

		/**
		 * 构造一个双数组搜索工具
		 *
		 * @param offset
		 *            搜索的起始位置
		 * @param charArray
		 *            搜索的目标字符数组
		 */
		public Searcher(char[] charArray, int offset) {
			this.charArray = charArray;
			i = offset;
			last = base[0];
			arrayLength = charArray.length;
			// A trick，如果文本长度为0的话，调用next()时，会带来越界的问题。
			// 所以我要在第一次调用next()的时候触发begin == arrayLength进而返回false。
			// 当然也可以改成begin >= arrayLength，不过我觉得操作符>=的效率低于==
			if (arrayLength == 0) {
				begin = -1;
			} else {
				begin = offset;
			}				
		}

		/**
		 * 取出下一个命中输出
		 *
		 * @return 是否命中，当返回false表示搜索结束，否则使用公开的成员读取命中的详细信息
		 */
		public boolean next() {
			int b = last;
			int n;
			int p;

			for (;; ++i) {
				if (i == arrayLength) // 指针到头了，将起点往前挪一个，重新开始，状态归零
				{
					++begin;
					if (begin == arrayLength)
						break;
					i = begin;
					b = base[0];
				}
				p = b + (int) (charArray[i]) + 1; // 状态转移 p = base[char[i-1]] + char[i] + 1
				if (b == check[p]) { // base[char[i-1]] == check[base[char[i-1]] + char[i] + 1]
					b = base[p]; // 转移成功
				} else {
					i = begin; // 转移失败，也将起点往前挪一个，重新开始，状态归零
					++begin;
					if (begin == arrayLength) {
						break;
					}
					b = base[0];
					continue;
				}
				p = b;
				n = base[p];
				if (b == check[p] && n < 0) { // base[p] == check[p] && base[p] < 0 查到一个词
					length = i - begin + 1;
					index = -n - 1;
					value = v[index];
					last = b;
					++i;
					return true;
				}
			}

			return false;
		}
	}

	public Searcher getSearcher(String text, int offset) {
		return new Searcher(text.toCharArray(), offset);
	}

	public Searcher getSearcher(char[] text, int offset) {
		return new Searcher(text ,offset);
	}	

}
