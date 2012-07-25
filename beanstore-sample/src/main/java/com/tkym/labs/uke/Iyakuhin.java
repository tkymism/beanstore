package com.tkym.labs.uke;

import com.tkym.labs.beanmeta.ano.Attribute;
import com.tkym.labs.beanmeta.ano.Key;
import com.tkym.labs.beanmeta.ano.Model;

@Model
public class Iyakuhin {
	/** 1.変更区分  */
	@Attribute
	private int henkouKubun;
	/** 2.マスター種別 */
	@Attribute
	private String syubetsu;
	/** 3.医薬品コード */
	@Key
	private int iyakuhinCode;
	/** 4.医薬品名・規格名-(漢字有効桁数)- */
	@Attribute
	private int kanjiKetasuu;
	/** 5.医薬品名・規格名-(漢字名称)- */
	@Attribute
	private String kanjiMeisyou;
	/** 6.医薬品名・規格名-(カナ有効桁数)- */
	@Attribute
	private int kanaKetasuu;
	/** 7.医薬品名・規格名-(カナ名称)- */
	@Attribute
	private String kanaName;
	/** 8.単位-コード */
	@Attribute
	private int taniCode;
	/** 9.単位-漢字有効桁数 */
	@Attribute
	private int taniKetasuu;
	/** 10.単位-漢字名称 */
	@Attribute
	private String taniName;
	/** 11.金額種別 */
	@Attribute
	private byte kingakuSyubetsu;
	/** 12.金額 */
	@Attribute
	private double kingaku;
	/** 13.予備 */
	@Attribute
	private String yobi1;
	/** 14.麻薬・毒薬・覚せい剤原料・向精神薬 */
	@Attribute
	private int mayaku;
	/** 15.神経破壊剤 */
	@Attribute
	private int shinkei;
	/** 16.生物学的製剤 */
	@Attribute
	private int seibutsu;
	/** 17.後発品 */
	@Attribute
	private int kouhatsu;
	/** 18.予備 */
	@Attribute
	private String yobi2;
	/** 19.歯科特定薬剤 */
	@Attribute
	private int shikaTokutei;
	/** 20.造影（補助）剤 */
	@Attribute
	private int zoueizai;
	/** 21.注射容量 */
	@Attribute
	private double youryou;
	/** 22.収載方式等識別 */
	@Attribute
	private int syuusai;
	/** 23.商品名等関関連 */
	@Attribute
	private double syouhinmeiKanren;
	/** 24.旧金額種別 */
	@Attribute
	private int kyuuKingakuSyubetsu;
	/** 25.旧金額 */
	@Attribute
	private double kyuuKingaku;
	/** 26.漢字名称変更区分 */
	@Attribute
	private int kanjiHenkou;
	/** 27.カナ名称変更区分 */
	@Attribute
	private int kanaHenkou;
	/** 28.剤形 */
	@Attribute
	private int zaikei;
	/** 29.予備 */
	@Attribute
	private String yobi3;
	/** 30.変更年月日 */
	@Attribute
	private int henkouDate;
	/** 31.廃止年月日 */
	@Attribute
	private int haishiDate;
	/** 32.薬価基準コード */
	@Attribute
	private String yakkaKijunCode;
	/** 33.公表順序番号 */
	@Attribute
	private String kouhyouJyunjo;
	/** 34.経過措置年月日又は商品名医薬品コード使用期限 */
	@Attribute
	private int keikasochi;
	public int getHenkouKubun() {
		return henkouKubun;
	}
	public void setHenkouKubun(int henkouKubun) {
		this.henkouKubun = henkouKubun;
	}
	public String getSyubetsu() {
		return syubetsu;
	}
	public void setSyubetsu(String syubetsu) {
		this.syubetsu = syubetsu;
	}
	public int getIyakuhinCode() {
		return iyakuhinCode;
	}
	public void setIyakuhinCode(int iyakuhinCode) {
		this.iyakuhinCode = iyakuhinCode;
	}
	public int getKanjiKetasuu() {
		return kanjiKetasuu;
	}
	public void setKanjiKetasuu(int kanjiKetasuu) {
		this.kanjiKetasuu = kanjiKetasuu;
	}
	public String getKanjiMeisyou() {
		return kanjiMeisyou;
	}
	public void setKanjiMeisyou(String kanjiMeisyou) {
		this.kanjiMeisyou = kanjiMeisyou;
	}
	public int getKanaKetasuu() {
		return kanaKetasuu;
	}
	public void setKanaKetasuu(int kanaKetasuu) {
		this.kanaKetasuu = kanaKetasuu;
	}
	public String getKanaName() {
		return kanaName;
	}
	public void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}
	public int getTaniCode() {
		return taniCode;
	}
	public void setTaniCode(int taniCode) {
		this.taniCode = taniCode;
	}
	public int getTaniKetasuu() {
		return taniKetasuu;
	}
	public void setTaniKetasuu(int taniKetasuu) {
		this.taniKetasuu = taniKetasuu;
	}
	public String getTaniName() {
		return taniName;
	}
	public void setTaniName(String taniName) {
		this.taniName = taniName;
	}
	public byte getKingakuSyubetsu() {
		return kingakuSyubetsu;
	}
	public void setKingakuSyubetsu(byte kingakuSyubetsu) {
		this.kingakuSyubetsu = kingakuSyubetsu;
	}
	public double getKingaku() {
		return kingaku;
	}
	public void setKingaku(double kingaku) {
		this.kingaku = kingaku;
	}
	public String getYobi1() {
		return yobi1;
	}
	public void setYobi1(String yobi1) {
		this.yobi1 = yobi1;
	}
	public int getMayaku() {
		return mayaku;
	}
	public void setMayaku(int mayaku) {
		this.mayaku = mayaku;
	}
	public int getShinkei() {
		return shinkei;
	}
	public void setShinkei(int shinkei) {
		this.shinkei = shinkei;
	}
	public int getSeibutsu() {
		return seibutsu;
	}
	public void setSeibutsu(int seibutsu) {
		this.seibutsu = seibutsu;
	}
	public int getKouhatsu() {
		return kouhatsu;
	}
	public void setKouhatsu(int kouhatsu) {
		this.kouhatsu = kouhatsu;
	}
	public String getYobi2() {
		return yobi2;
	}
	public void setYobi2(String yobi2) {
		this.yobi2 = yobi2;
	}
	public int getShikaTokutei() {
		return shikaTokutei;
	}
	public void setShikaTokutei(int shikaTokutei) {
		this.shikaTokutei = shikaTokutei;
	}
	public int getZoueizai() {
		return zoueizai;
	}
	public void setZoueizai(int zoueizai) {
		this.zoueizai = zoueizai;
	}
	public double getYouryou() {
		return youryou;
	}
	public void setYouryou(double youryou) {
		this.youryou = youryou;
	}
	public int getSyuusai() {
		return syuusai;
	}
	public void setSyuusai(int syuusai) {
		this.syuusai = syuusai;
	}
	public double getSyouhinmeiKanren() {
		return syouhinmeiKanren;
	}
	public void setSyouhinmeiKanren(double syouhinmeiKanren) {
		this.syouhinmeiKanren = syouhinmeiKanren;
	}
	public int getKyuuKingakuSyubetsu() {
		return kyuuKingakuSyubetsu;
	}
	public void setKyuuKingakuSyubetsu(int kyuuKingakuSyubetsu) {
		this.kyuuKingakuSyubetsu = kyuuKingakuSyubetsu;
	}
	public double getKyuuKingaku() {
		return kyuuKingaku;
	}
	public void setKyuuKingaku(double kyuuKingaku) {
		this.kyuuKingaku = kyuuKingaku;
	}
	public int getKanjiHenkou() {
		return kanjiHenkou;
	}
	public void setKanjiHenkou(int kanjiHenkou) {
		this.kanjiHenkou = kanjiHenkou;
	}
	public int getKanaHenkou() {
		return kanaHenkou;
	}
	public void setKanaHenkou(int kanaHenkou) {
		this.kanaHenkou = kanaHenkou;
	}
	public int getZaikei() {
		return zaikei;
	}
	public void setZaikei(int zaikei) {
		this.zaikei = zaikei;
	}
	public String getYobi3() {
		return yobi3;
	}
	public void setYobi3(String yobi3) {
		this.yobi3 = yobi3;
	}
	public int getHenkouDate() {
		return henkouDate;
	}
	public void setHenkouDate(int henkouDate) {
		this.henkouDate = henkouDate;
	}
	public int getHaishiDate() {
		return haishiDate;
	}
	public void setHaishiDate(int haishiDate) {
		this.haishiDate = haishiDate;
	}
	public String getYakkaKijunCode() {
		return yakkaKijunCode;
	}
	public void setYakkaKijunCode(String yakkaKijunCode) {
		this.yakkaKijunCode = yakkaKijunCode;
	}
	public String getKouhyouJyunjo() {
		return kouhyouJyunjo;
	}
	public void setKouhyouJyunjo(String kouhyouJyunjo) {
		this.kouhyouJyunjo = kouhyouJyunjo;
	}
	public int getKeikasochi() {
		return keikasochi;
	}
	public void setKeikasochi(int keikasochi) {
		this.keikasochi = keikasochi;
	}
}
