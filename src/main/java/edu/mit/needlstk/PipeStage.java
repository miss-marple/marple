package edu.mit.needlstk;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

public class PipeStage {
  /// Type of query operation, e.g., filter, map, etc.
  private OperationType op;

  /// Configuration info corresponding to the pipe type.
  private PipeConfigInfo configInfo;

  /// Name of the query corresponding to this stage from the original user program
  private String pipeName;

  /// List of fields in the result of this query operation
  private ArrayList<String> fields;

  public PipeStage(OperationType op, PipeConfigInfo configInfo) {
    this.op = op;
    this.configInfo = configInfo;
    this.fields = new ArrayList<String>();
  }

  @Override public String toString() {
    String res = this.pipeName;
    res += "\nSchema ";
    res += this.fields.toString();
    res += "\n";
    res += this.op.toString();
    if (this.op == OperationType.GROUPBY) {
      res += " ";
      res += ((FoldConfigInfo)this.configInfo).getKeyFields().toString();
      res += "\n";
      res += "Registers used: ";
      res += ((FoldConfigInfo)this.configInfo).getStateArgs().toString();
      res += "\n--";
    }
    res += "\n";
    // res += configInfo.print();
    res += configInfo.getP4();
    res += "\n";
    return res;
  }

  public String getP4Fragment() {
    String res = this.pipeName;
    res += "\n";
    res += this.fields.toString();
    res += "\n";
    res += this.op.toString();
    res += "\n";
    if (this.op == OperationType.GROUPBY) {
      FoldConfigInfo fci = (FoldConfigInfo)this.configInfo;
      List<String> fieldList = fci.getKeyFields().stream().
          map(var -> P4Printer.p4Ident(var, AggFunVarType.FIELD)).
          collect(Collectors.toList());
      res += fieldList.toString();
      res += "\n";
      res += fci.getStateArgs().toString();
      res += "\n";
    } else {
      res += "\n\n";
    }
    res += "--\n";
    // res += configInfo.print();
    res += configInfo.getP4();
    return res;
  }

  public String getDominoFragment() {
    return configInfo.getDomino();
  }

  public PipeConfigInfo getConfigInfo() {
    return configInfo;
  }

  public String getPipeName() {
    return this.pipeName;
  }

  public void setPipeName(String name) {
    this.pipeName = name;
  }

  public void addFields(ArrayList<String> fields) {
    this.fields.addAll(fields);
  }

  public HashSet<String> getSetFields() {
    return configInfo.getSetFields();
  }

  public HashSet<String> getUsedFields() {
    return configInfo.getUsedFields();
  }

  public OperationType getOp() {
    return this.op;
  }
}
