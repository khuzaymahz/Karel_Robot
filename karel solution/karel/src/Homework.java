import stanford.karel.SuperKarel;

import java.awt.*;

public class Homework extends SuperKarel {
    private Color fillColor;
    private int stepsCount, beepersLeft;
    private int[] dimensions;
    private int minDim, maxDim, chamberSize, squareSideLength, outerSplitLength;
    private int outerSplitOdd, minDimOdd, maxDimOdd;
    private boolean lengthLessThanWidth, turnDirection;

    public void run() {
        initialize();
        divideMap();
        printStats();
    }

    private void initialize() {
        initializeKarel();
        findDims();
        determineMinFromMaxDim();
        necessaryCalculations();
        setupDivision();
    }

    private void initializeKarel() {
        beepersLeft = 1000;
        setBeepersInBag(beepersLeft);
        stepsCount = 0;
        dimensions = new int[2];
    }

    private void findDims() {
        for (int i = 0; i <= 1; i++) {
            while(frontIsClear())
                stepForward();
            dimensions[i] = stepsCount+i+1;
            turnLeft();
        }
        dimensions[1] = dimensions[1] - dimensions[0];
    }

    private void determineMinFromMaxDim() {
        minDim = dimensions[0];
        maxDim = dimensions[1];
        lengthLessThanWidth = dimensions[0] < dimensions[1];
        if(lengthLessThanWidth) return;
        minDim = dimensions[1];
        maxDim = dimensions[0];
        turnLeft();
    }

    private void necessaryCalculations() {
        minDimOdd = minDim % 2;
        maxDimOdd = maxDim % 2;
        squareSideLength = minDim - 2;
        chamberSize = (squareSideLength - (2 - minDimOdd)) / 2 - 1;
        outerSplitLength = (maxDim - squareSideLength) / 2 + (maxDimOdd & ~minDimOdd);
        outerSplitOdd = outerSplitLength % 2;
    }

    private void setupDivision() {
        if (minDim < 7) {
            throw new IllegalArgumentException("The minimum dimension cannot be less than 7 units.");
        }
        moveStepsForward(chamberSize + 2);
        turnDirection = lengthLessThanWidth ? outerSplitOdd == 0 : outerSplitOdd == 1;
    }

    private void divideMap() {
        nearOuterChamberSplit();
        placeHalfBorder();
        farOuterChamberSplit("first");
        placeHalfBorder();
        adjustToSplitPosition();
        innerChambersSplit();
        farOuterChamberSplit("last");
    }

    private void placeHalfBorder() {
        fillColor = Color.yellow;
        placeL(chamberSize);
        stepForward();
        if(maxDimOdd == 1) oddAdjustment(1);
        if(maxDimOdd == 0) evenAdjustment(1);
        stepForward();
        placeL(chamberSize);
    }

    private void nearOuterChamberSplit() {
        fillColor = Color.blue;
        if(minDimOdd == 1){
            turnRightElseLeft(!lengthLessThanWidth);
            fillLine(outerSplitLength);
            stepForward();
        }
        if(minDimOdd == 0) alternate();
        turnRightElseLeft(!turnDirection);
        stepForward();
    }

    private void farOuterChamberSplit(String mode) {
        fillColor = Color.cyan;
        if(mode.equals("last") && minDimOdd == 1) {
            stepForward();
            fillLine(outerSplitLength +1);
        }
        if(mode.equals("first")) {
            if(minDimOdd == 1) moveStepsForward(2);
            if(minDimOdd == 0){
                stepForward();
                evenAdjustment(outerSplitLength);
                stepForward();
            }
        }
    }

    private void adjustToSplitPosition() {
        fillColor = Color.orange;
        stepForward();
        placeBeeper();
        turnRightElseLeft(turnDirection);
        stepForward();
    }

    private void innerChambersSplit() {
        fillColor = Color.red;
        if(minDimOdd == 0 && maxDimOdd == 1) adjustmentSplit();
        else if(minDimOdd == 0 && maxDimOdd == 0) windmillSplit();
        else oddSplit();
    }

    private void oddSplit() {
        fillLine(chamberSize);
        stepForward();
        for (int i = 0; i < 2; i++) {
            if(maxDimOdd == 1) oddAdjustment(chamberSize);
            if(maxDimOdd == 0) evenAdjustment(chamberSize);
            turnAround();
        }
        stepForward();
        if(maxDimOdd == 0) stepForward();
        fillLine(chamberSize);
    }

    private void adjustmentSplit() {
        fillLine(chamberSize);
        stepForward();
        turnDirection = !turnDirection;
        oddAdjustment(chamberSize);
        turnRightElseLeft(turnDirection);
        evenAdjustment(chamberSize);
        turnRightElseLeft(turnDirection);
        oddAdjustment(chamberSize);
        stepForward();
        fillLine(chamberSize);
    }


    private void windmillSplit() {
        for(int i = 1; true; i++) {
            placeL(chamberSize);
            if(i == 4) break;
            turnRightElseLeft(!turnDirection);
            stepForward();
            turnRightElseLeft(!turnDirection);
        }
    }

    private void alternate() {
        placeBeeper();
        stepForward();
        turnRightElseLeft(!lengthLessThanWidth);
        boolean switchLane = !lengthLessThanWidth;
        for(int i = 1; true; i++) {
            placeBeeper();
            stepForward();
            placeBeeper();
            if (i == outerSplitLength)
                break;
            turnRightElseLeft(switchLane);
            stepForward();
            turnRightElseLeft(!switchLane);
            switchLane = !switchLane;
        }
    }

    private void placeL(int length) {
        fillLine(length + 1);
        turnRightElseLeft(turnDirection);
        stepForward();
        fillLine(length);
    }

    private void evenAdjustment(int length) {
        turnRightElseLeft(!turnDirection);
        fillLine(length+1);
        turnRightElseLeft(turnDirection);
        stepForward();
        turnRightElseLeft(turnDirection);
        fillLine(length+1);
        turnRightElseLeft(!turnDirection);
    }

    private void oddAdjustment(int length) {
        turnRightElseLeft(!turnDirection);
        fillLine(length+1);
        turnAround();
        moveStepsForward(length);
        turnRightElseLeft(!turnDirection);
    }

    private void printStats(){
        System.out.println("Square side length: " + squareSideLength);
        System.out.printf("Chamber size: %d x %d\n", chamberSize, chamberSize);
        System.out.println("Outer split length: " + outerSplitLength);
        System.out.println("Steps count: " + stepsCount);
        System.out.println("Beepers left: " + beepersLeft);
        System.out.println("****************");
    }

    private void fillLine(int length){
        placeBeeper();
        for (int i = 1; i <= length-1; i++) {
            stepForward();
            placeBeeper();
        }
    }
    private void moveStepsForward(int steps) {
        for (int i = 1; i <= steps; i++)
            stepForward();
    }

    private void stepForward() {
        move();
        if(noBeepersPresent()) paintCorner(Color.green);
        stepsCount++;
    }

    private void placeBeeper(){
        if(beepersPresent()) return;
        putBeeper();
        paintCorner(fillColor);
        beepersLeft--;
    }

    private void turnRightElseLeft (boolean isRight){
        if(isRight) turnRight();
        else turnLeft();
    }
}