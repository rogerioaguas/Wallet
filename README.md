# Wallet [![Build Status](https://travis-ci.org/bcolombini/Wallet.svg?branch=master)](https://travis-ci.org/bcolombini/Wallet) [![codecov](https://codecov.io/gh/bcolombini/Wallet/branch/master/graph/badge.svg)](https://codecov.io/gh/bcolombini/Wallet) ![version](https://img.shields.io/badge/version-1.0.0-blue.svg)


## Arquitetura MVP

![MVP](https://image.slidesharecdn.com/m0btwb4rkmr1zmky6qn7-signature-88edf7dc82aa3b7d1972fa299b2092c0e0a267bfc9ffffec7e62e88691139bde-poli-160118153854/95/angular-js-16-638.jpg?cb=1453131618)

Utilizada no com Dagger2, RXKotlin e o framework ROOM da Google.

### Adicionar nova moeda

Não há necessidade de criar novas activitys, como estou utilizando fragment, basta adicionar o endpoint para ser feita a chamda da nova moeda, alterar 2 enums no projeto e alterar o exchange fragment.
 

# Desafio Mobile

O desafio consiste em criar uma carteira virtual de criptomoedas. Todo cliente ao se cadastrar recebe R$ 100.000,00 (cem mil reais) em conta para comprar Bitcoins e Britas.
A cotação da criptomoeda Brita é equivalente ao dólar e pode ser consultada na [API do Banco Central](https://dadosabertos.bcb.gov.br/dataset/taxas-de-cambio-todos-os-boletins-diarios) enquanto que a cotação do Bitcoin pode ser consultada na [API do Mercado Bitcoin](https://www.mercadobitcoin.net/api-doc/).

Sobre as operações financeiras de criptomoedas, é correto afirmar que:

* O cliente pode vender suas criptomoedas ou trocar uma pela outra. :heavy_check_mark:

* O cliente precisa saber o saldo discriminado por cada moeda. :heavy_check_mark:

* O cliente precisa ter um extrato de operações financeiras. :heavy_check_mark:

## Requisitos Técnicos

* O desafio deve ser feito em Swift, Kotlin, Objective-C ou Java. -> Kotlin
* Os dados precisam ser armazenados em um banco de dados local. -> ROOM

## Critérios de Avaliação

O desafio será avaliado através de cinco critérios.

### Entrega

* O código possui algum controle de dependências? -> Gradle 
* O resultado final está completo para ser executado? :heavy_check_mark:
* O resultado final atende ao que se propõe fazer? :heavy_check_mark:
* O resultado final atende totalmente aos requisitos propostos? :heavy_check_mark:
* O resultado final é visualmente elegante? :heavy_check_mark:

### Boas Práticas

* O código está de acordo com o guia de estilo da linguagem? :heavy_check_mark:
* O código está bem estruturado? :heavy_check_mark:
* O código está fluente na linguagem? :heavy_check_mark:
* O código faz o uso correto de _Design Patterns_? -> MVP

### Documentação

* O código foi entregue com um arquivo de README claro de como se guiar? :heavy_check_mark:
* O código possui comentários pertinentes? :heavy_check_mark:
* O código está em algum controle de versão? -> GIT
* Os commits são pequenos e consistentes? :heavy_check_mark:
* As mensagens de commit são claras? :heavy_check_mark:

### Código Limpo

* O código possibilita expansão para novas funcionalidades? Como seria o caso de acrescentar novas criptomoedas? :heavy_check_mark:
* O código é _Don't Repeat Yourself_? :heavy_check_mark:
* O código é fácil de compreender? :heavy_check_mark:

### Controle de Qualidade

* O código possui configuração de lint? :heavy_check_mark:
* O código possui testes unitários? :heavy_check_mark:
* O código possui cobertura de testes? :heavy_check_mark:
* O código está em Integração Contínua?  :heavy_check_mark:

## Material de Estudo
* [Boas Práticas na Stone](https://github.com/stone-payments/stoneco-best-practices/blob/master/README_pt.md)

## Licença
```
MIT License

Copyright (c) 2016 Stone Pagamentos

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
