package org.jjche.common.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * 单复数转换类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-15
 */
public class InflectorUtil {
    private static final Pattern UNDERSCORE_PATTERN_1 = Pattern.compile("([A-Z]+)([A-Z][a-z])");
    private static final Pattern UNDERSCORE_PATTERN_2 = Pattern.compile("([a-z\\d])([A-Z])");

    private static List<RuleAndReplacement> plurals = new ArrayList<RuleAndReplacement>();
    private static List<RuleAndReplacement> singulars = new ArrayList<RuleAndReplacement>();
    private static List<String> uncountables = new ArrayList<String>();

    private static InflectorUtil instance;

    private InflectorUtil() {
        initialize();
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        // 单数转复数
        System.out.println(InflectorUtil.getInstance().pluralize("water"));
        System.out.println(InflectorUtil.getInstance().pluralize("box"));
        System.out.println(InflectorUtil.getInstance().pluralize("tomato"));
        System.out.println(InflectorUtil.getInstance().pluralize("user_tomato"));
        // 复数转单数
        System.out.println(InflectorUtil.getInstance().singularize("apples"));
    }

    /**
     * <p>Getter for the field <code>instance</code>.</p>
     *
     * @return a {@link InflectorUtil} object.
     */
    public static InflectorUtil getInstance() {
        if (instance == null) {
            instance = new InflectorUtil();
        }
        return instance;
    }

    /**
     * <p>
     * /
     * </p>
     *
     * @param rule        /
     * @param replacement /
     */
    public static void plural(String rule, String replacement) {
        /**
         * <p>Setter for the field <code>rule</code>.</p>
         *
         * @param rule a {@link java.lang.String} object.
         */
        plurals.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * <p>singular.</p>
     *
     * @param rule        a {@link java.lang.String} object.
     * @param replacement a {@link java.lang.String} object.
     */
    public static void singular(String rule, String replacement) {
        singulars.add(0, new RuleAndReplacement(rule, replacement));
    }

    /**
     * <p>irregular.</p>
     *
     * @param singular a {@link java.lang.String} object.
     * @param plural   a {@link java.lang.String} object.
     */
    public static void irregular(String singular, String plural) {
        plural(singular, plural);
        singular(plural, singular);
    }

    /**
     * <p>uncountable.</p>
     *
     * @param words a {@link java.lang.String} object.
     */
    public static void uncountable(String... words) {
        for (String word : words) {
            uncountables.add(word);
        }
    }

    private void initialize() {
        plural("$", "s");
        plural("s$", "s");
        plural("(ax|test)is$", "$1es");
        plural("(octop|vir)us$", "$1i");
        plural("(alias|status)$", "$1es");
        plural("(bu)s$", "$1es");
        plural("(buffal|tomat)o$", "$1oes");
        plural("([ti])um$", "$1a");
        plural("sis$", "ses");
        plural("(?:([^f])fe|([lr])f)$", "$1$2ves");
        plural("(hive)$", "$1s");
        plural("([^aeiouy]|qu)y$", "$1ies");
        plural("([^aeiouy]|qu)ies$", "$1y");
        plural("(x|ch|ss|sh)$", "$1es");
        plural("(matr|vert|ind)ix|ex$", "$1ices");
        plural("([m|l])ouse$", "$1ice");
        plural("(ox)$", "$1es");
        plural("(quiz)$", "$1zes");

        singular("s$", "");
        singular("(n)ews$", "$1ews");
        singular("([ti])a$", "$1um");
        singular("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
        singular("(^analy)ses$", "$1sis");
        singular("([^f])ves$", "$1fe");
        singular("(hive)s$", "$1");
        singular("(tive)s$", "$1");
        singular("([lr])ves$", "$1f");
        singular("([^aeiouy]|qu)ies$", "$1y");
        singular("(s)eries$", "$1eries");
        singular("(m)ovies$", "$1ovie");
        singular("(x|ch|ss|sh)es$", "$1");
        singular("([m|l])ice$", "$1ouse");
        singular("(bus)es$", "$1");
        singular("(o)es$", "$1");
        singular("(shoe)s$", "$1");
        singular("(cris|ax|test)es$", "$1is");
        singular("([octop|vir])i$", "$1us");
        singular("(alias|status)es$", "$1");
        singular("^(ox)es", "$1");
        singular("(vert|ind)ices$", "$1ex");
        singular("(matr)ices$", "$1ix");
        singular("(quiz)zes$", "$1");

        irregular("person", "people");
        irregular("man", "men");
        irregular("child", "children");
        irregular("sex", "sexes");
        irregular("move", "moves");

        uncountable(new String[]{"equipment", "information", "rice", "money", "species", "series", "fish", "sheep"});
    }

    /**
     * <p>underscore.</p>
     *
     * @param camelCasedWord a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String underscore(String camelCasedWord) {

        String underscoredWord = UNDERSCORE_PATTERN_1.matcher(camelCasedWord).replaceAll("$1_$2");
        underscoredWord = UNDERSCORE_PATTERN_2.matcher(underscoredWord).replaceAll("$1_$2");
        underscoredWord = underscoredWord.replace('-', '_').toLowerCase();

        return underscoredWord;
    }

    /**
     * <p>
     * 单数转换复数
     * </p>
     *
     * @param word a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String pluralize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, plurals);
    }
/**
 * <p>Setter for the field <code>replacement</code>.</p>
 *
 * @param replacement a {@link java.lang.String} object.
 */

    /**
     * <p>singularize.</p>
     *
     * @param word a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String singularize(String word) {
        if (uncountables.contains(word.toLowerCase())) {
            return word;
        }
        return replaceWithFirstRule(word, singulars);
    }

    private String replaceWithFirstRule(String word, List<RuleAndReplacement> ruleAndReplacements) {

        for (RuleAndReplacement rar : ruleAndReplacements) {
            String rule = rar.getRule();
            String replacement = rar.getReplacement();

            // Return if we find a match.
            Matcher matcher = Pattern.compile(rule, Pattern.CASE_INSENSITIVE).matcher(word);
            if (matcher.find()) {
                return matcher.replaceAll(replacement);
            }
        }
        return word;
    }

    /**
     * <p>tableize.</p>
     *
     * @param className a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String tableize(String className) {
        return pluralize(underscore(className));
    }

    /**
     * /**
     * * <p>Constructor for RuleAndReplacement.</p>
     *
     * <p>Getter for the field <code>replacement</code>.</p>
     *
     * @param klass a {@link java.lang.Class} object.
     * @return a {@link java.lang.String} object.
     */
    public String tableize(Class<?> klass) {
        String className = klass.getName().replace(klass.getPackage().getName() + ".", "");
        return tableize(className);
    }
}

class RuleAndReplacement {
    private String rule;
    private String replacement;

    /**
     * <p>Constructor for RuleAndReplacement.</p>
     *
     * @param rule        a {@link java.lang.String} object.
     * @param replacement a {@link java.lang.String} object.
     */
    public RuleAndReplacement(String rule, String replacement) {
        this.rule = rule;
        this.replacement = replacement;
    }

    /**
     * <p>Getter for the field <code>replacement</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * <p>Setter for the field <code>replacement</code>.</p>
     *
     * @param replacement a {@link java.lang.String} object.
     */
    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    /**
     * <p>Getter for the field <code>rule</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getRule() {
        return rule;
    }

    /**
     * <p>Setter for the field <code>rule</code>.</p>
     *
     * @param rule a {@link java.lang.String} object.
     */
    public void setRule(String rule) {
        this.rule = rule;
    }
}
