package com.tkym.labs.beancache;

public interface BeancacheFilterMatcher<P> {
	boolean match(P index);
	public static class Matchers{
		public static BeancacheFilterMatcher<String> contain(final String str){
			return new BeancacheFilterMatcher<String>(){
				@Override
				public boolean match(String index) {
					if (index == null) return false;
					int i = index.indexOf(str);
					if (i >= 0) return true;
					else return false;
				}
			};
		}
		public static BeancacheFilterMatcher<String> endWith(final String str){
			return new BeancacheFilterMatcher<String>(){
				@Override
				public boolean match(String index) {
					if (index == null) return false;
					else return index.endsWith(str);
				}
			};
		}
		public static BeancacheFilterMatcher<String> startWith(final String str){
			return new BeancacheFilterMatcher<String>(){
				@Override
				public boolean match(String index) {
					if (index == null) return false;
					else return index.startsWith(str);
				}
			};
		}
	}
}
