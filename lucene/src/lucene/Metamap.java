package lucene;

//import java.util.List;
import java.util.*;

import gov.nih.nlm.nls.metamap.AcronymsAbbrevs;
import gov.nih.nlm.nls.metamap.ConceptPair;
import gov.nih.nlm.nls.metamap.Ev;
import gov.nih.nlm.nls.metamap.Mapping;
import gov.nih.nlm.nls.metamap.MetaMapApi;
import gov.nih.nlm.nls.metamap.MetaMapApiImpl;
import gov.nih.nlm.nls.metamap.Negation;
import gov.nih.nlm.nls.metamap.PCM;
import gov.nih.nlm.nls.metamap.Position;
import gov.nih.nlm.nls.metamap.Result;
import gov.nih.nlm.nls.metamap.Utterance;

public class Metamap {
	
	public static void main(String[] args) throws Exception {
	
		MetaMapApi api = new MetaMapApiImpl();//"C:/Users/NiraliPopat/Downloads/public_mm_win32_main_2014/public_mm/bin/metamap14");
	
		String termsAll= "Heart attack";//"in Vitro, and Thr269 of c-Raf-1 IsRequired--Thr269 is located within the CR2 domain ofc-Raf-1,";//"ReferencesTop Abstract Introduction Materials and Methods Results Discussion References   Sprent, J., and D.F. Tough. 1994. Lymphocyte life-span and memory. Science. 265:1395?1400.[Medline] Ahmed, R., and D. Gray. 1996. Immunological memory and protective immunity: understanding their relation. Science. 272:54?60.[Abstract] Theofilopoulos, A.N., and D.H. Kono. 1999. Murine lupus models: gene-specific and genome-wide studies. Systemic Lupus Erythematosus. R.G. Lahita, editor. Academic Press, San Diego. 145?181. Chu, E.B., D.N. Ernst, M.V. Hobbs, and W.O. Weigle. 1994. Maturational changes in CD4+ cell subsets and lymphokine production in BXSB mice. J. Immunol. 152:4129?4138.[Abstract/Free Full Text] Sabzevari, H., S. Propp, D.H. Kono, and A.N. Theofilopoulos. 1997. G1 arrest and high expression of cyclin kinase and apoptosis inhibitors in accumulated activated/memory phenotype CD4+ cells of older lupus mice. Eur. J. Immunol. 27:1901?1910.[Medline] Campisi, J. 1996. Replicative senescence: an old lives' tale? Cell. 84:497?500.[Medline] Noda, A., Y. Ning, S.F. Venable, O.M. Pereira-Smith, and J.R. Smith. 1994. Cloning of senescent cell-derived inhibitors of DNA synthesis using an expression screen. Exp. Cell Res. 211:90?98.[CrossRef][Medline] Sayama, K., Y. Shirakata, K. Midorikawa, Y. Hanakawa, and K. Hashimoto. 1999. Possible involvement of p21 but not of p16 or p53 in keratinocyte senescence. J. Cell. Physiol. 179:40?44.[CrossRef][Medline] Prud'homme, G.J., D.H. Kono, and A.N. Theofilopoulos. 1995. Quantitative polymerase chain reaction analysis reveals marked overexpression of interleukin-1ß, interleukin-1 and interferon- mRNA in the lymph nodes of lupus-prone mice. Mol. Immunol. 32:495?503.[CrossRef][Medline] Kozono, Y., B.L. Kotzin, and V.M. Holers. 1996. Resting B cells from New Zealand Black mice demonstrate a defect in apoptosis induction following surface IgM ligation. J. Immunol. 156:4498?4503.[Abstract] Wither, J.E., A.D. Paterson, and B. Vukusic. 2000. Genetic dissection of B cell traits in New Zealand black mice. The expanded population of B cells expressing up-regulated costimulatory molecules shows linkage to Nba2. Eur. J. Immunol. 30:356?365.[CrossRef][Medline] Blossom, S., E.B. Chu, W.O. Weigle, and K.M. Gilbert. 1997. CD40 ligand expressed on B cells in the BXSB mouse model of systemic lupus erythematosus. J. Immunol. 159:4580?4586.[Abstract] Theofilopoulos, A.N., and F.J. Dixon. 1985. Murine models of systemic lupus erythematosus. Adv. Immunol. 37:269?390.[Medline] Green, D.R., and D.W. Scott. 1994. Activation-induced apoptosis in lymphocytes. Curr. Opin. Immunol. 6:476?487.[CrossRef][Medline] Sherr, C.J. 1996. Cancer cell cycles. Science. 274:1672?1677.[Abstract/Free Full Text] Renno, T., A. Attinger, S. Locatelli, T. Bakker, S. Vacheron, and H.R. MacDonald. 1999. Cutting edge: apoptosis of superantigen-activated T cells occurs preferentially after a discrete number of cell divisions in vivo. J. Immunol. 162:6312?6315.[Abstract/Free Full Text] Boehme, S.A., and M.J. Lenardo. 1993. Propriocidal apoptosis of mature T lymphocytes occurs at S phase of the cell cycle. Eur. J. Immunol. 23:1552?1560.[Medline] Radvanyi, L.G., Y. Shi, G.B. Mills, and R.G. Miller. 1996. Cell cycle progression out of G1 sensitizes primary-cultured nontransformed T cells to TCR-mediated apoptosis. Cell. Immunol. 170:260?273.[CrossRef][Medline] Fotedar, R., J. Flatt, S. Gupta, R.L. Margolis, P. Fitzgerald, H. Messier, and A. Fotedar. 1995. Activation-induced T-cell death is cell cycle dependent and regulated by cyclin B. Mol. Cell. Biol. 15:932?942.[Abstract] Sherr, C.J., and J.M. Roberts. 1999. CDK inhibitors: positive and negative regulators of G1-phase progression. Genes Dev. 13:1501?1512.[Free Full Text] Dotto, G.P. 2000. p21(WAF1/Cip1): more than a break to the cell cycle? Biochim. Biophys. Acta. 1471:M43?M56.[CrossRef][Medline] Harada, K., and G.R. Ogden. 2000. An overview of the cell cycle arrest protein, p21(WAF1). Oral Oncol. 36:3?7.[CrossRef][Medline] Vousden, K.H. 2000. p53: death star. Cell. 103:691?694.[Medline] Gartel, A.L., and A.L. Tyner. 2002. The role of the cyclin-dependent kinase inhibitor p21 in apoptosis. Mol. Cancer Ther. 1:639?649.[Abstract/Free Full Text] Hobeika, A.C., W. Etienne, B.A. Torres, H.M. Johnson, and P.S. Subramaniam. 1999. IFN-gamma induction of p21(WAF1) is required for cell cycle inhibition and suppression of apoptosis. J. Interferon Cytokine Res. 19:1351?1361.[CrossRef][Medline] Xaus, J., M. Cardo, A.F. Valledor, C. Soler, J. Lloberas, and A. Celada. 1999. Interferon gamma induces the expression of p21waf-1 and arrests macrophage cell cycle, preventing induction of apoptosis. Immunity. 11:103?113.[Medline] Deng, C., P. Zhang, J.W. Harper, S.J. Elledge, and P. Leder. 1995. Mice lacking p21CIP1/WAF1 undergo normal development, but are defective in G1 checkpoint control. Cell. 82:675?684.[Medline] Le, L.Q., J.H. Kabarowski, Z. Weng, A.B. Satterthwaite, E.T. Harvill, E.R. Jensen, J.F. Miller, and O.N. Witte. 2001. Mice lacking the orphan G protein-coupled receptor G2A develop a late-onset autoimmune syndrome. Immunity. 14:561?571.[CrossRef][Medline] Lawson, B.R., G.J. Prud'homme, Y. Chang, H.A. Gardner, J. Kuan, D.H. Kono, and A.N. Theofilopoulos. 2000. Treatment of murine lupus with cDNA encoding IFN-R/Fc. J. Clin. Invest. 106:207?215.[Abstract/Free Full Text] Cheng, T., N. Rodrigues, H. Shen, Y. Yang, D. Dombkowski, M. Sykes, and D.T. Scadden. 2000. Hematopoietic stem cell quiescence maintained by p21cip1/waf1. Science. 287:1804?1808.[Abstract/Free Full Text] Okahashi, N., Y. Murase, T. Koseki, T. Sato, K. Yamato, and T. Nishihara. 2001. Osteoclast differentiation is associated with transient upregulation of cyclin-dependent kinase inhibitors p21(WAF1/CIP1) and p27(KIP1). J. Cell. Biochem. 80:339?345.[CrossRef][Medline] Fujio, Y., K. Guo, T. Mano, Y. Mitsuuchi, J.R. Testa, and K. Walsh. 1999. Cell cycle withdrawal promotes myogenic induction of Akt, a positive modulator of myocyte survival. Mol. Cell. Biol. 19:5073?5082.[Abstract/Free Full Text] Park, C.L., R.S. Balderas, T.M. Fieser, J.H. Slack, G.J. Prud'Homme, F.J. Dixon, and A.N. Theofilopoulos. 1983. Isotypic profiles and other fine characteristics of immune responses to exogenous thymus-dependent and -independent antigens by mice with lupus syndromes. J. Immunol. 130:2161?2167.[Free Full Text] Lawson, B.R., D.H. Kono, and A.N. Theofilopoulos. 2002. Deletion of p21 (WAF-1/Cip1) does not induce systemic autoimmunity in female BXSB mice. J. Immunol. 168:5928?5932.[Abstract/Free Full Text] Wofsy, D., C.E. Kerger, and W.E. Seaman. 1984. Monocytosis in the BXSB model for systemic lupus erythematosus. J. Exp. Med. 159:629?634.[Abstract] Balomenos, D., J. Martin-Caballero, M.I. Garcia, I. Prieto, J.M. Flores, M. Serrano, and A.C. Martinez. 2000. The cell cycle inhibitor p21 controls T-cell proliferation and sex-linked lupus development. Nat. Med. 6:171?176.[CrossRef][Medline] Santiago-Raber, M.L., B.R. Lawson, W. Dummer, M. Barnhouse, S. Koundouris, C.B. Wilson, D.H. Kono, and A.N. Theofilopoulos. 2001. Role of cyclin kinase inhibitor p21 in systemic autoimmunity. J. Immunol. 167:4067?4074.[Abstract/Free Full Text] Hengartner, M.O. 2000. The biochemistry of apoptosis. Nature. 407:770?776.[CrossRef][Medline] Krammer, P.H. 2000. CD95's deadly mission in the immune system. Nature. 407:789?795.[CrossRef][Medline] Glaser, T., B. Wagenknecht, and M. Weller. 2001. Identification of p21 as a target of cycloheximide-mediated facilitation of CD95-mediated apoptosis in human malignant glioma cells. Oncogene. 20:4757?4767.[CrossRef][Medline] Blossom, S.J., and K.M. Gilbert. 2000. B cells from autoimmune BXSB mice are hyporesponsive to signals provided by CD4+ T cells. Immunol. Invest. 29:287?297.[Medline] Blagosklonny, M.V. 2003. Cell senescence and hypermitogenic arrest. EMBO Rep. 4:358?362.[Abstract/Free Full Text] Marshall, C.J. 1995. Specificity of receptor tyrosine kinase signaling: transient versus sustained extracellular signal-regulated kinase activation. Cell. 80:179?185.[Medline] Woods, D., D. Parry, H. Cherwinski, E. Bosch, E. Lees, and M. McMahon. 1997. Raf-induced proliferation or cell cycle arrest is determined by the level of Raf activity with arrest mediated by p21Cip1. Mol. Cell. Biol. 17:5598?5611.[Abstract] Sewing, A., B. Wiseman, A.C. Lloyd, and H. Land. 1997. High-intensity Raf signal causes cell cycle arrest mediated by p21Cip1. Mol. Cell. Biol. 17:5588?5597.[Abstract] Kono, D.H., R. Baccala, and A.N. Theofilopoulos. 2004. Genes and genetics of murine lupus. Systemic Lupus Erythematosus. R.G. Lahita, editor. Academic Press, San Diego. In press. Vratsanos, G.S., S. Jung, Y.M. Park, and J. Craft. 2001. CD4(+) T cells from lupus-prone mice are hyperresponsive to T cell receptor engagement with low and high affinity peptide antigens: a model to explain spontaneous T cell activation in lupus. J. Exp. Med. 193:329?337.[Abstract/Free Full Text] Andris, F., M. Van Mechelen, F. De Mattia, E. Baus, J. Urbain, and O. Leo. 1996. Induction of T cell unresponsiveness by anti-CD3 antibodies occurs independently of co-stimulatory functions. Eur. J. Immunol. 26:1187?1195.[Medline] Miethke, T., C. Wahl, H. Gaus, K. Heeg, and H. Wagner. 1994. Exogenous superantigens acutely trigger distinct levels of peripheral T cell tolerance/immunosuppression: dose?response relationship. Eur. J. Immunol. 24:1893?1902.[Medline] Gilbert, K.M., and W.O. Weigle. 1993. Th1 cell anergy and blockade in G1a phase of the cell cycle. J. Immunol. 151:1245?1254.[Abstract/Free Full Text] Strauss, G., I. Knape, I. Melzner, and K.M. Debatin. 2003. Constitutive caspase activation and impaired death-inducing signaling complex formation in CD95-resistant, long-term activated, antigen-specific T cells. J. Immunol. 171:1172?1182.[Abstract/Free Full Text] Poluha, W., D.K. Poluha, B. Chang, N.E. Crosbie, C.M. Schonhoff, D.L. Kilpatrick, and A.H. Ross. 1996. The cyclin-dependent kinase inhibitor p21 (WAF1) is required for survival of differentiating neuroblastoma cells. Mol. Cell. Biol. 16:1335?1341.[Abstract] Marches, R., R. Hsueh, and J.W. Uhr. 1999. Cancer dormancy and cell signaling: induction of p21(waf1) initiated by membrane IgM engagement increases survival of B lymphoma cells. Proc. Natl. Acad. Sci. USA. 96:8711?8715.[Abstract/Free Full Text] Suzuki, A., Y. Tsutomi, K. Akahane, T. Araki, and M. Miura. 1998. Resistance to Fas-mediated apoptosis: activation of caspase 3 is regulated by cell cycle regulator p21WAF1 and IAP gene family ILP. Oncogene. 17:931?939.[CrossRef][Medline] Suzuki, A., Y. Tsutomi, M. Miura, and K. Akahane. 1999. Caspase 3 inactivation to suppress Fas-mediated apoptosis: identification of binding domain with p21 and ILP and inactivation machinery by p21. Oncogene. 18:1239?1244.[CrossRef][Medline] Suzuki, A., Y. Tsutomi, N. Yamamoto, T. Shibutani, and K. Akahane. 1999. Mitochondrial regulation of cell death: mitochondria are essential for procaspase 3-p21 complex formation to resist Fas-mediated cell death. Mol. Cell. Biol. 19:3842?3847.[Abstract/Free Full Text] Suzuki, A., T. Ito, H. Kawano, M. Hayashida, Y. Hayasaki, Y. Tsutomi, K. Akahane, T. Nakano, M. Miura, and K. Shiraki. 2000. Survivin initiates procaspase 3/p21 complex formation as a result of interaction with Cdk4 to resist Fas-mediated cell death. Oncogene. 19:1346?1353.[CrossRef][Medline] Gervais, J.L., P. Seth, and H. Zhang. 1998. Cleavage of CDK inhibitor p21(Cip1/Waf1) by caspases is an early event during DNA damage-induced apoptosis. J. Biol. Chem. 273:19207?19212.[Abstract/Free Full Text] Levkau, B., H. Koyama, E.W. Raines, B.E. Clurman, B. Herren, K. Orth, J.M. Roberts, and R. Ross. 1998. Cleavage of p21Cip1/Waf1 and p27Kip1 mediates apoptosis in endothelial cells through activation of Cdk2: role of a caspase cascade. Mol. Cell. 1:553?563.[Medline] Park, J.A., K.W. Kim, S.I. Kim, and S.K. Lee. 1998. Caspase 3 specifically cleaves p21WAF1/CIP1 in the earlier stage of apoptosis in SK-HEP-1 human hepatoma cells. Eur. J. Biochem. 257:242?248.[Abstract] Zhang, Y., N. Fujita, and T. Tsuruo. 1999. Caspase-mediated cleavage of p21Waf1/Cip1 converts cancer cells from growth arrest to undergoing apoptosis. Oncogene. 18:1131?1138.[CrossRef][Medline] Javelaud, D., and F. Besancon. 2002. Inactivation of p21WAF1 sensitizes cells to apoptosis via an increase of both p14ARF and p53 levels and an alteration of the Bax/Bcl-2 ratio. J. Biol. Chem. 277:37949?37954.[Abstract/Free Full Text] Botto, M., C. Dell'Agnola, A.E. Bygrave, E.M. Thompson, H.T. Cook, F. Petry, M. Loos, P.P. Pandolfi, and M.J. Walport. 1998. Homozygous C1q deficiency causes glomerulonephritis associated with multiple apoptotic bodies. Nat. Genet. 19:56?59.[Medline] Bickerstaff, M.C., M. Botto, W.L. Hutchinson, J. Herbert, G.A. Tennent, A. Bybee, D.A. Mitchell, H.T. Cook, P.J. Butler, M.J. Walport, and M.B. Pepys. 1999. Serum amyloid P component controls chromatin degradation and prevents antinuclear autoimmunity. Nat. Med. 5:694?697.[CrossRef][Medline] Wakeland, E.K., K. Liu, R.R. Graham, and T.W. Behrens. 2001. Delineating the genetic basis of systemic lupus erythematosus. Immunity. 15:397?408.[Medline] Mitchell, D.A., M.C. Pickering, J. Warren, L. Fossati-Jimack, J. Cortes-Hernandez, H.T. Cook, M. Botto, and M.J. Walport. 2002. C1q deficiency and autoimmunity: the effects of genetic background on disease expression. J. Immunol. 168:2538?2543.[Abstract/Free Full Text] Balomenos, D., R. Rumold, and A.N. Theofilopoulos. 1997. The proliferative in vivo activities of lpr double-negative T cells and the primary role of p59fyn in their activation and expansion. J. Immunol. 159:2265?2273.[Abstract] Gmelig-Meyling, F., S. Dawisha, and A.D. Steinberg. 1992. Assessment of in vivo frequency of mutated T cells in patients with systemic lupus erythematosus. J. Exp. Med. 175:297?300.[Abstract] Dayal, A.K., and G.M. Kammer. 1996. The T cell enigma in lupus. Arthritis Rheum. 39:23?33.[Medline] Goronzy, J.J., and C.M. Weyand. 2001. Thymic function and peripheral T-cell homeostasis in rheumatoid arthritis. Trends Immunol. 22:251?255.[CrossRef][Medline] Petersen, L.D., G. Duinkerken, G.J. Bruining, R.A. van Lier, R.R. de Vries, and B.O. Roep. 1996. Increased numbers of in vivo activated T cells in patients with recent onset insulin-dependent diabetes mellitus. J. Autoimmun. 9:731?737.[CrossRef][Medline] Smerdon, R.A., M. Peakman, M.J. Hussain, L. Alviggi, P.J. Watkins, R.D. Leslie, and D. Vergani. 1993. Increase in simultaneous coexpression of naive and memory lymphocyte markers at diagnosis of IDDM. Diabetes. 42:127?133.[Abstract] Arbogast, A., S. Boutet, M.A. Phelouzat, O. Plastre, R. Quadri, and J.J. Proust. 1999. Failure of T lymphocytes from elderly humans to enter the cell cycle is associated with low Cdk6 activity and impaired phosphorylation of Rb protein. Cell. Immunol. 197:46?54.[CrossRef][Medline]...";//"HA/CD44 MEDIATED SIGNALING PATHWAY|PKC PATHWAY|CD44/EZRIN/RADIXIN/MOESIN/ACTIN PATHWAY|CD44 DIRECTED CELL LYSIS";//"heart attack and myocardial infarction and SLE and lupus";//"DISEASES and STRAINS and GRAM POSITIVE ANAEROBES";//SIGNS OR SYMPTOMS and TOXICITIES and RENAL DYSFUNCTION and FATIGUE and ANEMIA and MYALGIA and FEVER and EDEMA"; //and CHEST PAIN and SHORTNESS OF BREATH and SWEATING and TACHYCARDIA and FAINTNESS";//"GENE and PATHWAY ICAM-1 TNF-ALPHA CD14 E-SELECTIN";
		String[] t = termsAll.split(",");
		//String terms = "";
		
	
		//terms = terms.replaceAll("\\|", " and ");
		api.setOptions("-y");//J genf,comd");
		int i=0;
		
		//System.out.println(i + "!!!!!!!!!!!!!!!!!! " + terms);
		//i++;
		Set<String> aset = new HashSet<>();
		//System.out.println("0.");
		List<Result> resultList = api.processCitationsFromString(termsAll.trim());
		//System.out.println(" 1.");
		Result result = resultList.get(0);
		List<AcronymsAbbrevs> aaList = result.getAcronymsAbbrevs();
		//System.out.println(" 2.");
		/*if (aaList.size() > 0) {
		  System.out.println("Acronyms and Abbreviations:");
		  for (AcronymsAbbrevs e: aaList) {
		    System.out.println("Acronym: " + e.getAcronym());
		    System.out.println("Expansion: " + e.getExpansion());
		    System.out.println("Count list: " + e.getCountList());
		    System.out.println("CUI list: " + e.getCUIList());
		  }
		} else {
		  System.out.println(" None.");
		}
		System.out.println(" 3 ");*/
		for (Utterance utterance: result.getUtteranceList()) {
			/*System.out.println("Utterance:");
			System.out.println(" Id: " + utterance.getId());
			System.out.println(" Utterance text: " + utterance.getString());
			System.out.println(" Position: " + utterance.getPosition());*/

			for (PCM pcm: utterance.getPCMList()) {
				System.out.println("Phrase:");
				System.out.println(" text: " + pcm.getPhrase().getPhraseText());
				
				//System.out.println("Candidates:");
				//System.out.println(pcm.getCandidateList());
		        for (Ev ev: pcm.getCandidateList()) {
		        	System.out.println(" 1");
		            System.out.println(" Candidate:");
		            System.out.println("  Score: " + ev.getScore());
		            System.out.println("  Concept Id: " + ev.getConceptId());
		            System.out.println("  Concept Name: " + ev.getConceptName());
		            System.out.println("  Preferred Name: " + ev.getPreferredName());
		            System.out.println("  Matched Words: " + ev.getMatchedWords());
		            System.out.println("  Semantic Types: " + ev.getSemanticTypes());
		            /*System.out.println("  MatchMap: " + ev.getMatchMap());
		            System.out.println("  MatchMap alt. repr.: " + ev.getMatchMapList());
		            System.out.println("  is Head?: " + ev.isHead());
		            System.out.println("  is Overmatch?: " + ev.isOvermatch());
		            System.out.println("  Sources: " + ev.getSources());
		            System.out.println("  Positional Info: " + ev.getPositionalInfo());*/
		        }
		        
		        System.out.println("Mappings:");
		        for (Mapping map: pcm.getMappingList()) {
		            System.out.println(" Map Score: " + map.getScore());
		            for (Ev mapEv: map.getEvList()) {
		              System.out.println("   Score: " + mapEv.getScore());
		              System.out.println("   Concept Id: " + mapEv.getConceptId());
		              System.out.println("   Concept Name: " + mapEv.getConceptName());
		              System.out.println("   Preferred Name: " + mapEv.getPreferredName());
		              System.out.println("   Matched Words: " + mapEv.getMatchedWords());
		              System.out.println("   Semantic Types: " + mapEv.getSemanticTypes());
		              
		              String[] cnames = mapEv.getPreferredName().split(",");
		              String cname =  cnames[0];
		              cnames = cname.split(":");
		              cname =  cnames[0];
		              cnames = cname.split("\\(");
		              cname =  cnames[0];
		              aset.add(cname.toLowerCase());
		              //System.out.println("----------" + cname);
		              
		              //System.out.println("   MatchMap: " + mapEv.getMatchMap());
		              //System.out.println("   MatchMap alt. repr.: " + mapEv.getMatchMapList());
		              //System.out.println("   is Head?: " + mapEv.isHead());
		              //System.out.println("   is Overmatch?: " + mapEv.isOvermatch());
		              //System.out.println("   Sources: " + mapEv.getSources());
		              //System.out.println("   Positional Info: " + mapEv.getPositionalInfo());
		            }
		          }
		    }
		}
		//System.out.println(aset);
	}
	
}


