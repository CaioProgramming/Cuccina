package com.inlustris.cuccina.feature.recipe.category.domain.model

import com.ilustris.cuccina.R

enum class Category(
    val icon: Int = R.drawable.cherry,
    val title: String,
    val description: String = "",
    val cover: String = ""
) {

    DRINKS(title = "Bebidas", description = DRINKS_DESCRIPTION,  icon = R.drawable.cocktail, cover = DRINKS_COVER),
    CANDY(title = "Confeitaria e doces", description = CANDY_DESCRIPTION,icon = R.drawable.candy, cover = CANDY_COVER),
    SOUPS(title = "Sopas e cremes", description = SOUPS_DESCRIPTION, icon = R.drawable.soup, cover = SOUPS_COVER),
    SEA(title = "Frutos do mar", description = SEA_DESCRIPTION, icon = R.drawable.crab, cover = SEA_COVER),
    VEGGIES(title = "Legumes e vegetais", description = VEGGIES_DESCRIPTION, icon = R.drawable.vegetable, cover = VEGGIES_COVER),
    PASTA(title = "Massas", description = PASTA_DESCRIPTION, icon = R.drawable.pasta, cover = PASTA_COVER),
    SAUCE(title = "Molhos", description = SAUCE_DESCRIPTION, icon = R.drawable.sauce, cover = SAUCE_COVER),
    PROTEIN(title = "Proteínas", description = PROTEIN_DESCRIPTION, icon = R.drawable.meat, cover = PROTEIN_COVER),
    SALADS(title = "Saladas", description = SALADS_DESCRIPTION, icon = R.drawable.salad, cover = SALADS_COVER),
    UNKNOWN(title = "Outros", description = UNKNOWN_DESCRIPTION, icon = R.drawable.cherry, cover = UNKNOWN_COVER)
}

const val DRINKS_COVER = "https://cdn.pixabay.com/photo/2018/04/12/14/51/drink-3313606_1280.jpg"
const val CANDY_COVER = "https://images.unsplash.com/photo-1555744038-d0bf77748106?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
const val SOUPS_COVER = "https://images.unsplash.com/photo-1616501268013-9a0ed08d2cbb?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
const val SEA_COVER = "https://cdn.pixabay.com/photo/2014/09/03/15/12/canker-434664_1280.jpg"
const val VEGGIES_COVER = "https://images.unsplash.com/photo-1606913079621-e64bd28682ba?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
const val PASTA_COVER = "https://images.unsplash.com/photo-1669295941291-080b4dbd3009?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1074&q=80"
const val SAUCE_COVER = "https://images.unsplash.com/photo-1612192666307-8dbd0c8a535a?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
const val PROTEIN_COVER = "https://cdn.pixabay.com/photo/2016/09/19/16/13/meat-1680616_1280.jpg"
const val SALADS_COVER = "https://images.unsplash.com/photo-1543339308-43e59d6b73a6?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1170&q=80"
const val UNKNOWN_COVER = "https://images.unsplash.com/photo-1608035910534-d29aea04943e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1131&q=80"


const val DRINKS_DESCRIPTION = "Descubra o segredo das bebidas irresistíveis! Prepare-se para uma explosão de sabores. Desde sucos energizantes a coquetéis exóticos, surpreenda-se com as receitas que vão mexer com o seu paladar. Satisfaça sua curiosidade e experimente combinações incríveis. Prepare-se para mergulhar em um mundo de sabores refrescantes. Aventure-se e descubra a bebida perfeita para cada momento."
const val CANDY_DESCRIPTION = "Bem-vindo ao paraíso dos doces! Embarque em uma jornada deliciosa com a categoria de Confeitaria. Descubra os segredos de sobremesas que vão deixar todos com água na boca. De bolos exuberantes a sobremesas cremosas, nossa coleção de receitas vai despertar seu lado doce e transformar suas criações em verdadeiras obras-primas. Prepare-se para se deliciar e impressionar com doces irresistíveis. Venha experimentar um mundo de sabores e texturas que farão você desejar mais a cada mordida. Seja um confeiteiro de sucesso e deixe sua imaginação voar alto. O doce mundo da confeitaria está à sua espera!"
const val SOUPS_DESCRIPTION = "Aqui, você encontrará um verdadeiro refúgio para os dias mais frios, onde cada colherada aquece o corpo e acalma a alma. Das clássicas sopas reconfortantes aos cremes luxuosos, cada receita é um abraço em forma de prato. Deixe-se envolver pelos aromas sedutores, pela textura reconfortante e pelos sabores que trazem memórias afetuosas à tona. Das sopas de legumes nutritivas aos cremes de cogumelos sedosos, cada opção é uma explosão de sabor que irá deliciar seu paladar. Permita-se explorar combinações únicas de ingredientes, temperos exóticos e técnicas culinárias que transformam simples sopas em obras de arte gastronômicas. Encontre a sopa perfeita para aquecer seu coração e conquistar seu estômago."
const val SEA_DESCRIPTION = "Descubra a elegância e o sabor exuberante dos frutos do mar. Delicie-se com a frescura do oceano e permita-se ser transportado para um mundo de sabores refinados. Convidamos você a explorar uma variedade de receitas sofisticadas, desde camarões suculentos até pratos de peixe delicadamente preparados. Deixe-se seduzir pela culinária marítima e descubra como os frutos do mar podem elevar suas experiências gastronômicas a um nível completamente novo. Aventure-se em uma jornada culinária que irá encantar seu paladar e satisfazer seus desejos mais requintados."
const val VEGGIES_DESCRIPTION = "Legumes e vegetais"
const val PASTA_DESCRIPTION = "Cada prato é uma ode à tradição e ao artesanato culinário, onde o cuidado e a paixão são ingredientes essenciais. Aqui, você encontrará uma vasta seleção de receitas que abraçam a diversidade das massas, desde os clássicos italianos até as criações contemporâneas inspiradas em diferentes culturas. Cada receita é meticulosamente elaborada, com orientações detalhadas e dicas preciosas para alcançar o ponto perfeito de cozimento e a combinação de sabores mais sublime. Das delicadas tagliatelles aos robustos rigatonis, das suculentas lasanhas aos recheios suaves dos tortellinis, cada prato de massa é uma jornada sensorial única. As opções de molhos, desde os tradicionais à base de tomate até os cremosos e aromáticos, enriquecem ainda mais a experiência culinária. Permita-se ser conduzido por um mundo de texturas envolventes, aromas sedutores e sabores cativantes, enquanto explora as inúmeras possibilidades que as massas oferecem."
const val SAUCE_DESCRIPTION = "De molhos clássicos a criações inovadoras, aqui você encontrará uma seleção irresistível para acompanhar massas, carnes, saladas e muito mais. São molhos que vão te fazer lamber os dedos e soltar aquele \"hummm\" de satisfação. Seja você um fã de molhos apimentados, cremosos, agridoces ou exóticos, temos opções para todos os gostos e paladares aventureiros. Permita-se explorar combinações ousadas, misturar sabores e criar suas próprias obras-primas gastronômicas. Afinal, molhos são como a cereja no topo do bolo - eles tornam tudo mais divertido e delicioso! "
const val PROTEIN_DESCRIPTION = "Descubra o poder das proteínas para um estilo de vida saudável e energético. As proteínas são essenciais para o nosso corpo, fornecendo os blocos de construção necessários para o crescimento muscular, a recuperação e a manutenção de um sistema imunológico forte. Nossa coleção de receitas oferece uma variedade de opções ricas em proteínas, desde pratos de carne magra e peixes saudáveis até receitas vegetarianas e veganas repletas de legumes, grãos e tofu. Descubra como as proteínas podem ajudar a manter seu corpo forte e saudável, proporcionando energia duradoura e promovendo a saciedade. Aumente seu bem-estar e explore a categoria de Proteínas para uma alimentação equilibrada e cheia de benefícios."
const val SALADS_DESCRIPTION = "Delicie-se com uma explosão de sabores leves e saudáveis que vão refrescar seu paladar. Com opções rápidas e fáceis de preparar, perfeitas para uma refeição leve ou como acompanhamento. Desfrute da conveniência de ingredientes frescos e coloridos, combinados de forma criativa para satisfazer seu apetite de maneira nutritiva. Experimente a leveza e a praticidade das saladas e adicione um toque de frescor à sua rotina alimentar."
const val UNKNOWN_DESCRIPTION = "Outros"