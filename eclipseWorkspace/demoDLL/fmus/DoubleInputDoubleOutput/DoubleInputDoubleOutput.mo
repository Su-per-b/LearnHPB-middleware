within ;
model DoubleInputDoubleOutput
  "Demo models for FMU with double input and double output"

public
  Modelica.Blocks.Math.Add add
    annotation (Placement(transformation(extent={{-40,20},{-20,40}})));

  Modelica.Blocks.Interfaces.RealInput u1 "Connector of Real input signal 1"
    annotation (Placement(transformation(extent={{-120,30},{-100,50}})));

  Modelica.Blocks.Math.Add add1(k2=-1)
    annotation (Placement(transformation(extent={{-40,-40},{-20,-20}})));

  Modelica.Blocks.Interfaces.RealInput u2 "Connector of Real input signal 1"
    annotation (Placement(transformation(extent={{-116,-48},{-100,-32}})));

  Modelica.Blocks.Sources.Constant const(k=1)
    annotation (Placement(transformation(extent={{-80,0},{-60,20}})));
  Modelica.Blocks.Sources.Constant const1(k=2)
    annotation (Placement(transformation(extent={{-80,-80},{-60,-60}})));

  Modelica.Blocks.Interfaces.RealOutput y1 "Connector of Real output signal"
    annotation (Placement(transformation(extent={{100,20},{119,39}})));

  Modelica.Blocks.Interfaces.RealOutput y2 "Connector of Real output signal"
    annotation (Placement(transformation(extent={{100,-40},{121,-19}})));
equation
  connect(add.u1, u1) annotation (Line(
      points={{-42,36},{-72,36},{-72,40},{-110,40}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(add1.u1, u2) annotation (Line(
      points={{-42,-24},{-72,-24},{-72,-40},{-108,-40}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(const.y, add.u2) annotation (Line(
      points={{-59,10},{-50,10},{-50,24},{-42,24}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(const1.y, add1.u2) annotation (Line(
      points={{-59,-70},{-50,-70},{-50,-36},{-42,-36}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(add.y, y1) annotation (Line(
      points={{-19,30},{40,30},{40,29.5},{109.5,29.5}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(add1.y, y2) annotation (Line(
      points={{-19,-30},{42,-30},{42,-29.5},{110.5,-29.5}},
      color={0,0,127},
      smooth=Smooth.None));
  annotation (uses(Modelica(version="3.2")), Diagram(graphics), Documentation(info="<html>
<p>
This model is designed for demonstrate the usage of functional mockup interface for cosimulation.
There are two input varibales and two output variables. 
It computes 
<li align=\"left\" style=\"font-style:italic;\">
<li>  y<sub>1</sub> = x<sub>1</sub> + 1, </li>
<li>  y<sub>2</sub> = x<sub>2</sub> - 2. </li>
</li>
</li>
</p>
</html>
", revisions="<html>
<ul>
<li>
Octobor 20, 2011, by Wangda Zuo:<br>
First implementation.
</li>
</ul>
</html>"));
end DoubleInputDoubleOutput;
