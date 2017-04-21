package com.poos.cop.schedulebuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class Tag {
	String type;
	String attribsStr;
	ArrayList<Tag> children;
	HashMap<String, String> attribs;
	String contents;

	public Tag() {
		type = null;
		children = new ArrayList<>();
		contents = "";
		attribsStr = "";
		attribs = new HashMap<>();
	}

	public void addChild(Tag t) {
		children.add(t);
	}
	public String getAttrib(String key) {
		String tmp = attribs.get(key);
		if(tmp == null) tmp = "";
		return tmp;
	}
	public static Tag parse(String str) {
		Text text = new Text("<root>" + str + "</root>");
		Tag root = parse(text);
		if(text.tempChildren != null) {
			root.children.addAll(text.tempChildren);
		}
		if(text.tempContents != null) {
			root.contents += text.tempContents;
		}

		text.tempChildren = null;
		text.tempContents = null;
		return root;
	}
	public static Tag parse(Text txt) {
		while(true) {
			if(txt.atEnd()) return null;
			char c = txt.peek();
			if(c == '<') {
				break;
			} else {
				txt.next();
				continue;
			}
		}

		boolean isOpenClose = false;

		txt.next(); // Eat the <
		if(txt.peek() == '/') {
			txt.prev(); // Go back
			return null;
		} else if(txt.peek() == '!') {
			txt.next();
			// Maybe a comment
			if(txt.next() == '-') {
				if(txt.next() == '-') {
					// Okay, definetely a comment
					boolean foundEnd = false;
					while(!txt.atEnd()) {
						if(txt.next() == '-') {
							if(txt.next() == '-') {
								if(txt.next() == '>') {
									foundEnd = true;
									break;
								}
							}
						}
					}

					if(!foundEnd) {
						System.out.println("Failed to find the end of a comment!!!");
					}

					return Tag.parse(txt);
				}
				txt.prev();
			}
			txt.prev();
			txt.prev();
		}
		String type = txt.nextToken().toLowerCase();
		StringBuilder sb = new StringBuilder();
		while(!txt.atEnd()) {
			if(txt.peek() == '>') break;
			sb.append(txt.next());
		}
		String attribs = sb.toString();
		if(attribs.length() > 0 && attribs.charAt(attribs.length() - 1) == '/') {
			attribs = attribs.substring(0, attribs.length() - 1);
			isOpenClose = true;
		}

		txt.next(); // Eat the >

		Tag t = new Tag();
		t.type = type;
		t.parseAttribs(attribs);
		t.contents = "";

		if(type.equals("img")) isOpenClose = true;

		if(isOpenClose) return t;

		int origI = txt.i;

		StringBuilder tempContents = new StringBuilder();
		ArrayList<Tag> tempChildren = new ArrayList<>();
		while(true) {
			while(!txt.atEnd() && txt.peek() != '<') {
				char c = txt.next();
				tempContents.append(c);
			}
			Tag child = Tag.parse(txt);
			if(child == null) break;
			if(txt.tempContents != null) {
				tempContents.append(txt.tempContents);
				txt.tempContents = null;
			}
			if(txt.tempChildren != null) {
				tempChildren.addAll(txt.tempChildren);
				txt.tempChildren = null;
			}
			tempChildren.add(child);
		}

		origI = txt.i;

		String closeTagType = "";
		if(!txt.atEnd()) {
			txt.next(); // eat <
			txt.next(); // eat /
			closeTagType = txt.nextToken().toLowerCase();
		}

		if(!closeTagType.equals(type)) {
			txt.tempContents = tempContents;
			txt.tempChildren = tempChildren;

			//System.out.printf("No end found for %s. Found %s instead.\n", type, closeTagType);

			txt.i = origI;
			return t;
		} else {
			t.contents = tempContents.toString().trim();
			t.children = tempChildren;
		}

		// Eat our matching close tag
		while(!txt.atEnd()) {
			if(txt.next() == '>') break;
		}

		return t;

	}

	public void parseAttribs(String str) {
		attribsStr = str;
		char[] arr = str.toCharArray();
		int i = 0;
		while(i < arr.length) {
			while(i < arr.length && arr[i] == ' ') i++;
			if(i >= arr.length) break;

			StringBuilder sb1 = new StringBuilder();
			while(i < arr.length && arr[i] != '=') sb1.append(arr[i++]);

			StringBuilder sb2 = new StringBuilder();
			i++;
			i++;
			while(i < arr.length && arr[i] != '"' && arr[i] != '\'') sb2.append(arr[i++]);
			i++;

			String key = sb1.toString();
			String val = sb2.toString();

			if(key.length() > 0) {
				attribs.put(key, val);
			}
		}
	}

	public Tag findById(String id) {
		String tid = attribs.get("id");
		if(tid != null && matches(tid, 0, id, 0)) return this;
		for(Tag t : children) {
			Tag tmp = t.findById(id);
			if(tmp != null) return tmp;
		}
		return null;
	}

	public boolean same(Tag t) {
		return type.equals(t.type);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append('<');
		sb.append(type);
		sb.append(" " + attribs.toString());
		sb.append('>');
		sb.append(contents);

		for(Tag t : children)
			sb.append(t.toString());

		sb.append("</");
		sb.append(type);
		sb.append(">");

		return sb.toString();
	}

	public String toString(int indent) {
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		for(int i = 0; i < indent; i++)
			sb2.append(' ');
		String in = sb2.toString();

		sb.append(in);
		sb.append('<');
		sb.append(type);
		sb.append(" id=" + attribs.get("id"));
		sb.append('>');
		sb.append('\n');

		for(Tag t : children) {
			sb.append(t.toString(indent + 2));
			sb.append('\n');
		}

		sb.append(in);
		sb.append("</");
		sb.append(type);
		sb.append(">");

		return sb.toString();
	}

	static class Text {
		public ArrayList<Tag> tempChildren;
		public StringBuilder tempContents;
		char[] str;
		int i;
		public Text(String strr) {
			str = strr.toCharArray();
			i = 0;
		}
		public char peek() {
			return str[i];
		}
		public void prev() {
			i--;
		}
		public char next() {
			return str[i++];
		}
		public String nextToken() {
			StringBuilder sb = new StringBuilder();
			if(!atEnd()) sb.append(next());
			while(!atEnd()) {
				if(!Character.isAlphabetic(peek()))
					break;
				sb.append(next());
			}
			return sb.toString();
		}
		public boolean atEnd() {
			return i == str.length;
		}
	}
	
	static boolean matches(String str, int i, String match, int j) {
        if(i == str.length()) return j == match.length();
        if(j == match.length()) return false;
        
        if(str.charAt(i) == match.charAt(j) || match.charAt(j) == '?') return matches(str, i + 1, match, j + 1);
        
        if(match.charAt(j) == '*') {
            for(int count = 0; count < str.length() - i; count++) {
                if(matches(str, i + count, match, j + 1)) return true;
            }
            return false;
        }
        
        return false;
    }
}