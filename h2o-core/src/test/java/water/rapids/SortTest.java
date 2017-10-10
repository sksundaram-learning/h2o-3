package water.rapids;

import org.junit.BeforeClass;
import org.junit.Test;
import water.*;
import water.fvec.*;
import water.nbhm.NonBlockingHashMapLong;
import water.rapids.vals.ValFrame;
import water.util.ArrayUtils;

import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertTrue;

public class SortTest extends TestUtil {
  @BeforeClass public static void setup() { stall_till_cloudsize(1); }

  @Test public void testBasicSortRapids() {
    Frame fr = null, res = null;

    // Stable sort columns 1 and 2
    String tree = "(sort hex [1 2] [1 1])";
    try {

      // Build a frame which is unsorted on small-count categoricals in columns
      // 0 and 1, and completely sorted on a record-number based column 2.
      // Sort will be on columns 0 and 1, in that order, and is expected stable.
      fr = buildFrame(1000,10);
      fr.insertVec(0,"row",fr.remove(2));
      //
      Val val = Rapids.exec(tree);
      assertTrue( val instanceof ValFrame);
      res = val.getFrame();
      res.add("row",res.remove(0));
      new CheckSort().doAll(res);
    } finally {
      if( fr  != null ) fr .delete();
      if( res != null ) res.delete();
    }
  }

  @Test public void testBasicSortJava() {
    Frame fr = null, res = null;
    try {
      fr = buildFrame(1000,10);
      fr.insertVec(0,"row",fr.remove(2));
      res = Merge.sort(fr,new int[]{1,2});
      res.add("row",res.remove(0));
      new CheckSort().doAll(res);
    } finally {
      if( fr  != null ) fr .delete();
      if( res != null ) res.delete();
    }
  }

/*
  @Test public void testGeneratedFrames4Merge() {
    Vec tempV;
    Random rand = new Random();
*/
/*    String[] allNames = {"/Users/wendycwong/temp/seed1.csv", "/Users/wendycwong/temp/merged1.csv",
            "/Users/wendycwong/temp/frameA1.csv", "/Users/wendycwong/temp/frameB1.csv",
            "/Users/wendycwong/temp/mergedLeft1.csv"};*//*

    String[] allNames = {"/Users/wendycwong/temp/seed2.csv", "/Users/wendycwong/temp/merged2.csv",
            "/Users/wendycwong/temp/frameA2.csv", "/Users/wendycwong/temp/frameB2.csv",
            "/Users/wendycwong/temp/mergedLeft2.csv"};
    Frame fr = parse_test_file(allNames[0]);
    //Frame sortedfr = fr.sort(new int[]{0}); // sorted merged frame
    double includeBothProb = 0.8;

    ArrayList<Integer> includeA = new ArrayList<Integer>();
    ArrayList<Integer> includeB = new ArrayList<Integer>();
    ArrayList<Integer> includeBoth = new ArrayList<Integer>();

    for (int index = 0; index < fr.numRows(); index++) {
      if (rand.nextDouble() > 0.5)
        includeA.add(index);  // pick up the row indices to be included in Frame B
      else
        includeB.add(index);

      if (rand.nextDouble() > includeBothProb) {
        includeBoth.add(index); // add to both Frame A and B
        if (!includeA.contains(index))
          includeA.add(index);

        if (!includeB.contains(index))
          includeB.add(index);
      }
    }

*/
/*    double[][] keyBoth = new double[1][includeBoth.size()];
    double[][] mergedBoth = new double[2][includeBoth.size()];
    fillArrays(keyBoth[0], mergedBoth, null, includeBoth, sortedfr, 0, false, null);
    double[][] tempKey = new double[1][keyBoth.length];
    Frame part1 = new water.util.ArrayUtils().frame(transpose(keyBoth));
    part1.add(new water.util.ArrayUtils().frame(transpose(mergedBoth)));
    try {
      writeFrameToCSV(allNames[1], part1, true, false);
    } catch (IOException e) {
      e.printStackTrace();
    }*//*


    double[][] keyA = new double[1][includeA.size()];
    double[][] colA = new double[1][includeA.size()];
    fillArrays(keyA[0], null, colA[0], includeA, fr, 1, false, null);
    Frame part2 = new water.util.ArrayUtils().frame(transpose(keyA));
    part2.add(new water.util.ArrayUtils().frame(transpose(colA)));
    try {
      writeFrameToCSV(allNames[2], part2, true, false);
    } catch (IOException e) {
      e.printStackTrace();
    }

    double[][] keyB = new double[1][includeB.size()];
    double[][] colB = new double[1][includeB.size()];
    fillArrays(keyB[0], null, colB[0], includeB, fr, 2, false, null);
    Frame part3 = new water.util.ArrayUtils().frame(transpose(keyB));
    part3.add(new water.util.ArrayUtils().frame(transpose(colB)));
    try {
      writeFrameToCSV(allNames[3], part3, true, false);
    } catch (IOException e) {
      e.printStackTrace();
    }

*/
/*    double[][] keyLeft = new double[1][includeA.size()];
    double[][] mergedLeftAll = new double[2][includeA.size()];
    fillArrays(keyLeft[0], mergedLeftAll, null, includeA, sortedfr, 2, true, includeBoth);
    Frame part4 = new water.util.ArrayUtils().frame(transpose(keyLeft));
    part4.add(new water.util.ArrayUtils().frame(transpose(mergedLeftAll)));
    try {
      writeFrameToCSV(allNames[4], part4, true, false);
    } catch (IOException e) {
      e.printStackTrace();
    }*//*

  }

  public void fillArrays(double[] keys, double[][] merged, double[] oneCol, ArrayList<Integer> rowIndices, Frame sourceF, int colInd, boolean addNAs, ArrayList<Integer> commRowIndices) {
    if (merged == null) {
      for (int index = 0; index < keys.length; index++) {
        int rowIndex = rowIndices.get(index);
        keys[index] = (int) sourceF.vec(0).at(rowIndex);
        oneCol[index] = sourceF.vec(colInd).at(rowIndex);
      }
    } else {
      for (int index = 0; index < keys.length; index++) {
        int rowIndex = rowIndices.get(index);
        keys[index] = sourceF.vec(0).at8(rowIndex);
        merged[0][index] = sourceF.vec(1).at(rowIndex);
        merged[1][index] = sourceF.vec(2).at(rowIndex);
      }

      if (addNAs) {
        for (int index=0; index < rowIndices.size(); index++) {
          int rowIndex = rowIndices.get(index);

          if (!commRowIndices.contains(rowIndex)) {
            merged[1][index] = Double.NaN;
          }
        }
      }
    }
  }
*/

  @Test public void testBasicSortJava2() {
    Frame fr = null, res = null;
    try {
      fr = buildFrame(1000,10);
      String[] domain = new String[1000];
      for( int i=0; i<1000; i++ ) domain[i] = "D"+i;
      fr.vec(0).setDomain(domain);
      res = fr.sort(new int[]{0,1});
      new CheckSort().doAll(res);
    } finally {
      if( fr  != null ) fr .delete();
      if( res != null ) res.delete();
    }
  }


  // Assert that result is indeed sorted - on all 3 columns, as this is a
  // stable sort.
  private class CheckSort extends MRTask<CheckSort> {
    @Override public void map( Chunk cs[] ) {
      long x0 = cs[0].at8(0);
      long x1 = cs[1].at8(0);
      long x2 = cs[2].at8(0);
      for( int i=1; i<cs[0]._len; i++ ) {
        long y0 = cs[0].at8(i);
        long y1 = cs[1].at8(i);
        long y2 = cs[2].at8(i);
        assertTrue(x0<y0 || (x0==y0 && (x1<y1 || (x1==y1 && x2<y2))));
        x0=y0; x1=y1; x2=y2;
      }
      // Last row of chunk is sorted relative to 1st row of next chunk
      long row = cs[0].start()+cs[0]._len;
      if( row < cs[0].vec().length() ) {
        long y0 = cs[0].vec().at8(row);
        long y1 = cs[1].vec().at8(row);
        long y2 = cs[2].vec().at8(row);
        assertTrue(x0<y0 || (x0==y0 && (x1<y1 || (x1==y1 && x2<y2))));
      }
    }
  }

  // Build a 3 column frame.  Col #0 is categorical with # of cats given; col
  // #1 is categorical with 10x more choices.  A set of pairs of col#0 and
  // col#1 is made; each pair is given about 100 rows.  Col#2 is a row number.
  private static Frame buildFrame( int card0, int nChunks ) {
    // Compute the pairs
    int scale0 = 3;    // approximate ratio actual pairs vs all possible pairs; so scale0=3/scale1=10 is about 30% actual unique pairs
    int scale1 = 10;   // scale of |col#1| / |col#0|, i.e., col#1 has 10x more levels than col#0
    int scale2 = 100;  // number of rows per pair
    if( nChunks == -1 ) {
      long len = (long)card0*(long)scale0*(long)scale2;
      int rowsPerChunk = 100000;
      nChunks = (int)((len+rowsPerChunk-1)/rowsPerChunk);
    }
    NonBlockingHashMapLong<String> pairs_hash = new NonBlockingHashMapLong<>();
    Random R = new Random(card0*scale0*nChunks);
    for( int i=0; i<card0*scale0; i++ ) {
      long pair = (((long)R.nextInt(card0))<<32) | (R.nextInt(card0*scale1));
      if( pairs_hash.containsKey(pair) ) i--; // Reroll dice on collisions
      else pairs_hash.put(pair,"");
    }
    long[] pairs = pairs_hash.keySetLong();

    Key[] keys = new Vec.VectorGroup().addVecs(3);
    AppendableVec col0 = new AppendableVec(keys[0], Vec.T_NUM);
    AppendableVec col1 = new AppendableVec(keys[1], Vec.T_NUM);
    AppendableVec col2 = new AppendableVec(keys[2], Vec.T_NUM);

    NewChunk ncs0[] = new NewChunk[nChunks];
    NewChunk ncs1[] = new NewChunk[nChunks];
    NewChunk ncs2[] = new NewChunk[nChunks];

    for( int i=0; i<nChunks; i++ ) {
      ncs0[i] = new NewChunk(col0,i);
      ncs1[i] = new NewChunk(col1,i);
      ncs2[i] = new NewChunk(col2,i);
    }

    // inject random pairs into cols 0 and 1
    int len = pairs.length*scale2;
    for( int i=0; i<len; i++ ) {
      long pair = pairs[R.nextInt(pairs.length)];
      int nchk = R.nextInt(nChunks);
      ncs0[nchk].addNum( (int)(pair>>32),0);
      ncs1[nchk].addNum( (int)(pair    ),0);
    }

    // Compute data layout
    int espc[] = new int[nChunks+1];
    for( int i=0; i<nChunks; i++ )
      espc[i+1] = espc[i] + ncs0[i].len();

    // Compute row numbers into col 2
    for( int i=0; i<nChunks; i++ )
      for( int j=0; j<ncs0[i].len(); j++ )
        ncs2[i].addNum(espc[i]+j,0);

    Futures fs = new Futures();
    for( int i=0; i<nChunks; i++ ) {
      ncs0[i].close(i,fs);
      ncs1[i].close(i,fs);
      ncs2[i].close(i,fs);
    }

    Vec vec0 = col0.layout_and_close(fs);
    Vec vec1 = col1.layout_and_close(fs);
    Vec vec2 = col2.layout_and_close(fs);
    fs.blockForPending();
    Frame fr = new Frame(Key.<Frame>make("hex"), null, new Vec[]{vec0,vec1,vec2});
    DKV.put(fr);
    return fr;
  }


  @Test public void testSortTimes() throws IOException {
    Scope.enter();
    Frame fr=null, sorted=null;
    try {
      fr = parse_test_file("sort_crash.csv");
      sorted = fr.sort(new int[]{0});
      Scope.track(fr);
      Scope.track(sorted);
      testSort(sorted, fr,0);
    } finally {
      Scope.exit();
    }
  }

  @Test public void testSortOverflows() throws IOException {
    Scope.enter();
    Frame fr=null, sorted=null;
    try {
      fr = ArrayUtils.frame(ar("Long", "Double"), ard(Long.MAX_VALUE, Double.MAX_VALUE),
              ard(Long.MIN_VALUE+10, Double.MIN_VALUE),
              ard(Long.MAX_VALUE, Double.MAX_VALUE), ard(-1152921504, Double.MIN_VALUE));
      int colIndex = 0;
      sorted = fr.sort(new int[]{colIndex}); // sort Long/integer first
      Scope.track(fr);
      Scope.track(sorted);
      testSort(sorted, fr,colIndex);
    } finally {
      Scope.exit();
    }
  }


  @Test public void testSortIntegersFloats() throws IOException {
    // test small integers sort
    testSortOneColumn("smalldata/synthetic/smallIntFloats.csv.zip", 0, false, false);
    // test small float sort
    testSortOneColumn("smalldata/synthetic/smallIntFloats.csv.zip", 1, false, false);
    // test integer frame
    testSortOneColumn("smalldata/synthetic/integerFrame.csv", 0, false, false);
    // test integer frame with NAs
    testSortOneColumn("smalldata/synthetic/integerFrame.csv", 0, true, false);
    // test double frame
    testSortOneColumn("smalldata/synthetic/doubleFrame.csv", 0, false, false);
    // test double frame with NAs
    testSortOneColumn("smalldata/synthetic/doubleFrame.csv", 0, true, false);
    // test integer frame where overflow will occur for col.max()-col.min()
  //  TestSortOneColumn("smalldata/synthetic/bigIntFloatsOverflows.csv.zip", 0, false, false);
    // test integer frame where overflow will occur for col.max()-col.min(), with NAs
  //  TestSortOneColumn("smalldata/synthetic/bigIntFloatsOverflows.csv.zip", 0, true, false);
    // test double frame where overflow will occur for col.max()-col.min()
  //  TestSortOneColumn("smalldata/synthetic/bigIntFloatsOverflows.csv.zip", 1, false, false);
    // test double frame where overflow will occur for col.max()-col.min(), with NAs
  //  TestSortOneColumn("smalldata/synthetic/bigIntFloatsOverflows.csv.zip", 1, true, false);
  }

  /*
  Test sorting of integers and floats of small magnitude, 2^30 and no NANs or INFs
 */
  private static void testSortOneColumn(String fileWithPath, int colIndex, boolean addNas, boolean addInfs) throws IOException {
    Scope.enter();
    Frame fr = null, sortedInt = null, sortedFloat = null;
    try {
      fr = parse_test_file(fileWithPath);
      if (addNas) {
        Random _rand = new Random();
        int randRange = Math.min(10, (int)fr.numRows());
        int numNAs = _rand.nextInt(randRange)+1;    // number of NAs to generate and insert

        for (int index = 0; index < numNAs; index++) {
          fr.vec(colIndex).setNA(_rand.nextInt((int)fr.numRows())); // insert NAs
        }
      }

      if (addInfs && fr.vec(colIndex).isNumeric() && !fr.vec(colIndex).isInt()) {
        Random _rand = new Random();
        int  infRange = Math.min(10, (int)fr.numRows());
        int numInfs = _rand.nextInt(infRange)+1;

        for (int index = 0; index < numInfs; index++) {
          fr.vec(colIndex).set(_rand.nextInt((int)fr.numRows()), Double.POSITIVE_INFINITY);
          fr.vec(colIndex).set(_rand.nextInt((int)fr.numRows()), Double.NEGATIVE_INFINITY);
        }
      }
      
      Scope.track(fr);
      sortedInt = fr.sort(new int[]{colIndex});
      Scope.track(sortedInt);
      testSort(sortedInt, fr, colIndex);
    } finally {
      Scope.exit();
    }
  }

  @Test public void testSortIntegersDescend() throws IOException {
    Scope.enter();
    Frame fr, sortedInt;
    try {
      fr = parse_test_file("smalldata/synthetic/integerFrame.csv");
      sortedInt = fr.sort(new int[]{0}, new int[]{-1});
      Scope.track(fr);
      Scope.track(sortedInt);

      long numRows = fr.numRows();
      assert numRows==sortedInt.numRows();
      for (long index = 1; index < numRows; index++) {
        assertTrue(sortedInt.vec(0).at8(index) >= sortedInt.vec(0).at8(index));
      }
    } finally {
      Scope.exit();
    }
  }

  private static void testSort(Frame frSorted, Frame originalF, int colIndex) throws IOException {
    Scope.enter();
    Vec vec = frSorted.vec(colIndex);
    Vec vecO = originalF.vec(colIndex);
    Scope.track(vec);
    Scope.track(vecO);
    long naCnt = 0;   // make sure NAs are sorted at the beginning of frame

    if (originalF.hasNAs()) {
      naCnt = vecO.naCnt();
    }

    try {
      // check size
      assertTrue(frSorted.numRows() == originalF.numRows());  // make sure sizes are the same
      assertTrue(vec.naCnt() == vecO.naCnt());                // NA counts agree
      assertTrue(vec.pinfs() == vecO.pinfs());                // inf number agree
      assertTrue(vec.ninfs() == vecO.ninfs());                // -inf number agree
      int len = (int) vec.length();
      // count the NAs first
      for (int i = 0; i < naCnt; i++) {
        assertTrue(Double.isNaN(vec.at(i)));
      }
      for (int i = 1; i < len; i++) {
        if (!Double.isNaN(vec.at(i - 1)) && !Double.isNaN(vec.at(i)))
          assertTrue(vec.at(i - 1) <= vec.at(i));
      }
    } finally {
      Scope.exit();
    }
  }
}
