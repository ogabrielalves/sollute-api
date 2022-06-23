package sollute.estoquecerto.entity;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ArquivoTxt {

    public static void gravaRegistro(String registro, String nomeArq) {

        BufferedWriter saida = null;

        try {
            saida = new BufferedWriter(new FileWriter(nomeArq, true));
        } catch (IOException erro) {
            System.out.println("Erro ao abrir arquivo" + erro);
        }

        try {
            saida.append(registro + "\n");
            saida.close();
        } catch (IOException erro) {
            System.out.println("Erro ao gravar o arquivo" + erro);
        }

    }


    public static void gravarArquivoTxt(List<Produto> lista, String nomeArq) {

        int contaRegCorpo = 0;

        String header = "00PRODUTOS";
        header += LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss"));
        header += "01";

        gravaRegistro(header, nomeArq);

        String corpo;
        for (Produto p : lista) {

            corpo = "02";
            corpo += String.format("%02d", p.getIdProduto());
            corpo += String.format("%-5.5s", p.getCodigo());
            corpo += String.format("%-30.30s", p.getNome());
            corpo += String.format("%-30.30s", p.getMarca());
            corpo += String.format("%-30.30s", p.getCategoria());
            corpo += String.format("%-2.2s", p.getTamanho());
            corpo += String.format("%04.2f", p.getPeso());
            corpo += String.format("%04.2f", p.getPrecoCompra());
            corpo += String.format("%04.2f", p.getPrecoVenda());
            corpo += String.format("%04d", p.getEstoque());
            corpo += String.format("%04d", p.getEstoqueMin());
            corpo += String.format("%04d", p.getEstoqueMax());
            corpo += String.format("%09d", p.getQtdVendidos());
            corpo += String.format("%09.2f", p.getValorVendidos());

            contaRegCorpo++;
            gravaRegistro(corpo, nomeArq);
        }

        String trailer = "01";
        trailer += String.format("%1d", contaRegCorpo);
        gravaRegistro(trailer, nomeArq);

    }

    public static void leArquivoTxt(String nomeArq) {

        BufferedReader entrada = null;
        String registro, tipoRegistro;
        Integer idProduto, estoque, estoqueMin, estoqueMax, qtdVendidos;
        String codigo, nome, marca, categoria, tamanho;
        Double peso, precoCompra, precoVenda, valorVendidos;
        int contaRegCorpoLido = 0;
        int qtdRegCorpoGravado;

        List<Produto> listaLida = new ArrayList<>();

        try {
            entrada = new BufferedReader(new FileReader(nomeArq));
        } catch (IOException erro) {
            System.out.println("Erro ao abrir o arquivo" + erro);
        }

        try {

            registro = entrada.readLine();

            while (registro != null) {

                tipoRegistro = registro.substring(0, 2);

                switch (tipoRegistro) {

                    case "00":

                        System.out.println("É um registro de Header");
                        System.out.println("Tipo de arquivo: " + registro.substring(2, 10));
                        System.out.println("Data e hora da gravação: " + registro.substring(10, 29));
                        System.out.println("Versão do documento: " + registro.substring(29, 31));
                        break;

                    case "01":

                        System.out.println("É um registro de Trailer");
                        qtdRegCorpoGravado = Integer.parseInt(registro.substring(2, 3));

                        if (contaRegCorpoLido == qtdRegCorpoGravado) {
                            System.out.println(
                                    "\nQuantidade de registros lidos é compativel com a quantidade de registros gravados");
                            registro = null;
                        } else {
                            System.out.println(contaRegCorpoLido);
                            System.out.println(qtdRegCorpoGravado);
                            System.out.println(
                                    "Quantidade de registros lidos não é compativel com a quantidade de registros gravados.");
                        }
                        break;

                    case "02":

                        contaRegCorpoLido++;
                        System.out.println("É um registro do corpo.");
                        idProduto = Integer.valueOf(registro.substring(2, 4));
                        codigo = registro.substring(4, 9).trim();
                        nome = registro.substring(9, 39).trim();
                        marca = registro.substring(39, 69).trim();
                        categoria = registro.substring(69, 99).trim();
                        tamanho = registro.substring(99, 101).trim();
                        peso = Double.valueOf(registro.substring(101, 105).replace(',', '.'));
                        precoCompra = Double.valueOf(registro.substring(105, 110).replace(',', '.'));
                        precoVenda = Double.valueOf(registro.substring(110, 116).replace(',', '.'));
                        estoque = Integer.valueOf(registro.substring(116, 121));
                        estoqueMin = Integer.valueOf(registro.substring(121, 125));
                        estoqueMax = Integer.valueOf(registro.substring(125, 129));
                        qtdVendidos = Integer.valueOf(registro.substring(129, 138));
                        valorVendidos = Double.valueOf(registro.substring(138, 145).replace(',', '.'));

                        Produto p = new Produto(
                                idProduto,
                                codigo,
                                nome,
                                marca,
                                categoria,
                                tamanho,
                                peso,
                                precoCompra,
                                precoVenda,
                                estoque,
                                estoqueMin,
                                estoqueMax,
                                qtdVendidos,
                                valorVendidos);

                        listaLida.add(p);

                        break;

                    default:
                        System.out.println("Tipo de registro inválido");
                        break;
                }
                registro = entrada.readLine();
            }
        } catch (IOException erro) {
            System.out.println("Erro ao ler o arquivo " + erro);
        }

    }

    public static void main(String[] args) {

        List<Produto> lista = new ArrayList<>();

        lista.add(new Produto(
                1,
                "01",
                "Vestido Branco",
                "Renner",
                "Roupas",
                "G1",
                0.5,
                100.0,
                30.0,
                50,
                10,
                999,
                275,
                1500.0));

        lista.add(new Produto(
                2,
                "02",
                "Camiseta Preta",
                "Lost",
                "Roupas",
                "GG",
                0.5,
                120.0,
                250.0,
                350,
                10,
                999,
                500,
                7500.0));

        gravarArquivoTxt(lista, "produto.txt");

        leArquivoTxt("produto.txt");

    }

}
