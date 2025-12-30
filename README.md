# Markovian Candidate

A Java-based machine learning project that uses **Markov chains** to analyze and classify political text based on writing style.

## Overview

This project builds probabilistic Markov models from political speeches and uses them to determine which candidate is more likely to have authored an unseen piece of text. It demonstrates how language patterns can be captured statistically and applied to classification tasks.

## Features

- Builds Markov models from training text
- Compares likelihoods across multiple candidates
- Classifies unknown text based on learned language patterns
- Simple and clear Java implementation of Markov chains

## Project Structure

- `Main.java` — Entry point and execution logic
- `MarkovModel.java` — Core Markov chain implementation
- `BestModel.java` — Model comparison and selection logic
- `bush.txt`, `kerry.txt` — Training datasets
- `test_*.txt` — Evaluation and mixed test samples

## How It Works

1. Train a Markov model on known candidate text
2. Generate probability distributions of character transitions
3. Score unseen text against each model
4. Select the most likely author

## Requirements

- Java 8 or higher

## Usage

Compile and run using:

```bash
javac *.java
java Main
