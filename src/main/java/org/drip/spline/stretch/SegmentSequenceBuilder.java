
package org.drip.spline.stretch;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * <i>SegmentSequenceBuilder</i> is the interface that contains the stubs required for the construction of
 * the segment stretch. It exposes the following functions:
 *
 * <br><br>
 *  <ul>
 *  	<li>
 * 			Set the Stretch whose Segments are to be calibrated
 *  	</li>
 *  	<li>
 * 			Retrieve the Calibration Boundary Condition
 *  	</li>
 *  	<li>
 * 			Calibrate the Starting Segment using the LeftSlope
 *  	</li>
 *  	<li>
 * 			Calibrate the Segment Sequence in the Stretch
 *  	</li>
 *  </ul>
 *
 * <br><br>
 *  <ul>
 *		<li><b>Project</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline">Spaces</a></li>
 *		<li><b>Package</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline/stretch">Stretch</a></li>
 *		<li><b>Specification</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal/SplineBuilder">Spline Builder Library</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public interface SegmentSequenceBuilder {

	/**
	 * Set the Stretch whose Segments are to be calibrated
	 * 
	 * @param mss The Stretch that needs to be calibrated
	 * 
	 * @return TRUE - Stretch successfully set
	 */

	public abstract boolean setStretch (
		final org.drip.spline.stretch.MultiSegmentSequence mss);

	/**
	 * Retrieve the Calibration Boundary Condition
	 * 
	 * @return The Calibration Boundary Condition
	 */

	public abstract org.drip.spline.stretch.BoundarySettings getCalibrationBoundaryCondition();

	/**
	 * Calibrate the Starting Segment using the LeftSlope
	 * 
	 * @param dblLeftSlope The Slope
	 * 
	 * @return TRUE - The Segment was successfully set up.
	 */

	public abstract boolean calibStartingSegment (
		final double dblLeftSlope);

	/**
	 * Calibrate the Segment Sequence in the Stretch
	 * 
	 * @param iStartingSegment The Starting Segment in the Sequence
	 * 
	 * @return TRUE - The Segment Sequence successfully calibrated
	 */

	public abstract boolean calibSegmentSequence (
		final int iStartingSegment);

	/**
	 * Compute the Stretch Manifest Measure Sensitivity Sequence
	 * 
	 * @param dblLeftSlopeSensitivity The Leading Segment Left Slope Sensitivity
	 * 
	 * @return TRUE - The Stretch Manifest Measure Sensitivity Sequence successfully computed
	 */

	public boolean manifestMeasureSensitivity (
		final double dblLeftSlopeSensitivity);
}
