within ;
model SimpleGain

  Modelica.Blocks.Math.Gain gain(k=2)
    annotation (Placement(transformation(extent={{-20,-10},{0,10}})));
  Modelica.Blocks.Interfaces.RealInput u(
    final quantity="Temperature",
    final unit="K",
    min = 273.15,
    displayUnit="degC") "Input signal connector"
    annotation (Placement(transformation(extent={{-140,-20},{-100,20}})));
  Modelica.Blocks.Interfaces.RealOutput y(
    final quantity="Temperature",
    final unit="K",
    min = 273.15,
    displayUnit="degC") "Output signal connector"
    annotation (Placement(transformation(extent={{100,-10},{120,10}})));
equation
  connect(gain.u, u) annotation (Line(
      points={{-22,0},{-120,0}},
      color={0,0,127},
      smooth=Smooth.None));
  connect(gain.y, y) annotation (Line(
      points={{1,0},{110,0}},
      color={0,0,127},
      smooth=Smooth.None));
  annotation (uses(Modelica(version="3.2")), Diagram(graphics));
end SimpleGain;
