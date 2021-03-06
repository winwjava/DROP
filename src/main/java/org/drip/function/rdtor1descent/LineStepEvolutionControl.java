
package org.drip.function.rdtor1descent;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, and portfolio construction within and across fixed income, credit, commodity, equity,
 *  	FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three main modules:
 *  
 *  - DROP Analytics Core - https://lakshmidrip.github.io/DROP-Analytics-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Numerical Core - https://lakshmidrip.github.io/DROP-Numerical-Core/
 * 
 * 	DROP Analytics Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Asset Backed Analytics
 * 	- XVA Analytics
 * 	- Exposure and Margin Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Numerical Core implements libraries for the following:
 * 	- Statistical Learning Library
 * 	- Numerical Optimizer Library
 * 	- Machine Learning Library
 * 	- Spline Builder Library
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>LineStepEvolutionControl</i> contains the Parameters required to compute the Valid a Line Step. The
 * References are:
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Armijo, L. (1966): Minimization of Functions having Lipschitz-Continuous First Partial
 * 				Derivatives <i>Pacific Journal of Mathematics</i> <b>16 (1)</b> 1-3
 * 		</li>
 * 		<li>
 * 			Nocedal, J., and S. Wright (1999): <i>Numerical Optimization</i> <b>Wiley</b>
 * 		</li>
 * 		<li>
 * 			Wolfe, P. (1969): Convergence Conditions for Ascent Methods <i>SIAM Review</i> <b>11 (2)</b>
 * 				226-235
 * 		</li>
 * 		<li>
 * 			Wolfe, P. (1971): Convergence Conditions for Ascent Methods; II: Some Corrections <i>SIAM
 * 				Review</i> <b>13 (2)</b> 185-188
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/README.md">Function</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/rdtor1descent/README.md">R<sup>d</sup> To R<sup>1</sup></a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class LineStepEvolutionControl {
	private int _iNumReductionStep = -1;
	private double _dblReductionFactor = java.lang.Double.NaN;
	private org.drip.function.rdtor1descent.LineEvolutionVerifier _lev = null;

	/**
	 * Retrieve the Nocedal-Wright-Armijo Verifier Based Standard LineStepEvolutionControl Instance
	 * 
	 * @param bMaximizerCheck TRUE - Perform a Check for the Function Maxima
	 * 
	 * @return The Nocedal-Wright-Armijo Verifier Based Standard LineStepEvolutionControl Instance
	 */

	public static final LineStepEvolutionControl NocedalWrightArmijo (
		final boolean bMaximizerCheck)
	{
		try {
			return new LineStepEvolutionControl
				(org.drip.function.rdtor1descent.ArmijoEvolutionVerifier.NocedalWrightStandard
					(bMaximizerCheck), 0.75, 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Nocedal-Wright-Weak Curvature Verifier Based Standard LineStepEvolutionControl Instance
	 * 
	 * @return The Nocedal-Wright-Weak Curvature Verifier Based Standard LineStepEvolutionControl Instance
	 */

	public static final LineStepEvolutionControl NocedalWrightWeakCurvature()
	{
		try {
			return new LineStepEvolutionControl
				(org.drip.function.rdtor1descent.CurvatureEvolutionVerifier.NocedalWrightStandard (false),
					0.75, 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Nocedal-Wright-Strong Curvature Verifier Based Standard LineStepEvolutionControl Instance
	 * 
	 * @return The Nocedal-Wright-Strong Curvature Verifier Based Standard LineStepEvolutionControl Instance
	 */

	public static final LineStepEvolutionControl NocedalWrightStrongCurvature()
	{
		try {
			return new LineStepEvolutionControl
				(org.drip.function.rdtor1descent.CurvatureEvolutionVerifier.NocedalWrightStandard (true),
					0.75, 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Nocedal-Wright-Weak Wolfe Verifier Based Standard LineStepEvolutionControl Instance
	 * 
	 * @param bMaximizerCheck TRUE - Perform a Check for the Function Maxima
	 * 
	 * @return The Nocedal-Wright-Weak Wolfe Verifier Based Standard LineStepEvolutionControl Instance
	 */

	public static final LineStepEvolutionControl NocedalWrightWeakWolfe (
		final boolean bMaximizerCheck)
	{
		try {
			return new LineStepEvolutionControl
				(org.drip.function.rdtor1descent.WolfeEvolutionVerifier.NocedalWrightStandard
					(bMaximizerCheck, false), 0.75, 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Retrieve the Nocedal-Wright-Strong Wolfe Verifier Based Standard LineStepEvolutionControl Instance
	 * 
	 * @param bMaximizerCheck TRUE - Perform a Check for the Function Maxima
	 * 
	 * @return The Nocedal-Wright-Strong Wolfe Verifier Based Standard LineStepEvolutionControl Instance
	 */

	public static final LineStepEvolutionControl NocedalWrightStrongWolfe (
		final boolean bMaximizerCheck)
	{
		try {
			return new LineStepEvolutionControl
				(org.drip.function.rdtor1descent.WolfeEvolutionVerifier.NocedalWrightStandard
					(bMaximizerCheck, true), 0.75, 1);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * LineStepEvolutionControl Constructor
	 * 
	 * @param lev The Lie Evolution Verifier Instance
	 * @param dblReductionFactor The Per-Step Reduction Factor
	 * @param iNumReductionStep Number of Reduction Steps
	 * 
	 * @throws java.lang.Exception Thrown if Inputs are Invalid
	 */

	public LineStepEvolutionControl (
		final org.drip.function.rdtor1descent.LineEvolutionVerifier lev,
		final double dblReductionFactor,
		final int iNumReductionStep)
		throws java.lang.Exception
	{
		if (null == (_lev = lev) || !org.drip.numerical.common.NumberUtil.IsValid (_dblReductionFactor =
			dblReductionFactor) || 1. <= _dblReductionFactor || 0 >= (_iNumReductionStep =
				iNumReductionStep))
			throw new java.lang.Exception ("LineStepEvolutionControl Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Line Evolution Verifier Instance
	 * 
	 * @return The Line Evolution Verifier Instance
	 */

	public org.drip.function.rdtor1descent.LineEvolutionVerifier lineEvolutionVerifier()
	{
		return _lev;
	}

	/**
	 * Retrieve the Reduction Factor per Step
	 * 
	 * @return The Reduction Factor per Step
	 */

	public double reductionFactor()
	{
		return _dblReductionFactor;
	}

	/**
	 * Retrieve the Number of Reduction Steps
	 * 
	 * @return The Number of Reduction Steps
	 */

	public int reductionSteps()
	{
		return _iNumReductionStep;
	}
}
