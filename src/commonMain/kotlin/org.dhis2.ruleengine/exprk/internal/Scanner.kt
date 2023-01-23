package org.dhis2.ruleengine.exprk.internal

import org.dhis2.ruleengine.exprk.ExpressionException
import org.dhis2.ruleengine.exprk.internal.TokenType.*

private fun invalidToken(c: Char) {
    throw ExpressionException("Invalid token '$c'")
}

internal class Scanner(
    private val source: String
) {

    private val tokens: MutableList<Token> = mutableListOf()
    private var start = 0
    private var current = 0
    private var variableNameStarted = false

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            scanToken()
        }

        tokens.add(Token(EOF, "", null))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        start = current
        val c = advance()

        when (c) {
            ' ',
            '\r',
            '\t' -> {
                // Ignore whitespace.
            }

            '+' -> addToken(PLUS)
            '-' -> addToken(MINUS)
            '*' -> addToken(STAR)
            '/' -> addToken(SLASH)
            '%' -> addToken(MODULO)
            '^' -> addToken(EXPONENT)
            '√' -> addToken(SQUARE_ROOT)
            '=' -> if (match('=')) addToken(EQUAL_EQUAL) else addToken(ASSIGN)
            '!' -> if (match('=')) addToken(NOT_EQUAL) else invalidToken(c)
            '>' -> if (match('=')) addToken(GREATER_EQUAL) else addToken(GREATER)
            '<' -> if (match('=')) addToken(LESS_EQUAL) else addToken(LESS)
            '|' -> if (match('|')) addToken(BAR_BAR) else invalidToken(c)
            '&' -> if (match('&')) addToken(AMP_AMP) else invalidToken(c)
            ',' -> addToken(COMMA)
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '\'' -> addToken(LITERAL)
            else -> {
                when {
                    c.isDigit() -> number()
                    c.isAlpha() -> identifier()
                    else -> invalidToken(c)
                }
            }
        }
    }

    private fun isDigit(
        char: Char,
        previousChar: Char = '\u0000',
        nextChar: Char = '\u0000'
    ): Boolean {
        return char.isDigit() || when (char) {
            '.' -> true
            'e', 'E' -> previousChar.isDigit() && (nextChar.isDigit() || nextChar == '+' || nextChar == '-')
            '+', '-' -> (previousChar == 'e' || previousChar == 'E') && nextChar.isDigit()
            else -> false
        }
    }

    private fun number() {
        while (peek().isDigit()) advance()

        if (isDigit(peek(), peekPrevious(), peekNext())) {
            advance()
            while (isDigit(peek(), peekPrevious(), peekNext())) advance()
        }

        val value = source
            .substring(start, current)

        addToken(NUMBER, value)
    }

    private fun identifier() {
        when {
            isVariableName(peekPrevious(), peek()) -> variableNameStarted = true
            source[current] == '}' -> variableNameStarted = false
        }
        while (variableNameStarted || peek().isAlphaNumeric()){
            advance()
            if(isVariableNameEnd(peek())){
                variableNameStarted = false
            }
        }

        addToken(IDENTIFIER)
    }

    private fun isVariableName(
        char: Char,
        nextChar: Char = '\u0000'
    ): Boolean {
        return "$char$nextChar" == "A{"
                || "$char$nextChar" == "#{"
                || "$char$nextChar" == "V{"
                || "$char$nextChar" == "C{"
    }

    private fun isVariableNameEnd(char:Char):Boolean{
        return peek() == '}'
    }

    private fun advance() = source[current++]

    private fun peek(): Char {
        return if (isAtEnd()) {
            '\u0000'
        } else {
            source[current]
        }
    }

    private fun peekPrevious(): Char = if (current > 0) source[current - 1] else '\u0000'

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) {
            '\u0000'
        } else {
            source[current + 1]
        }
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++
        return true
    }

    private fun addToken(type: TokenType) = addToken(type, null)

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal))
    }

    private fun Char.isAlphaNumeric() = isAlpha() || isDigit()

    private fun Char.isAlpha() = this in 'a'..'z'
            || this in 'A'..'Z'
            || this == '_'
            || this == ':'
            || this == '#'
            || this == '{'
            || this == '}'
            || this == ';'

    private fun Char.isDigit() = this == '.' || this in '0'..'9'
}