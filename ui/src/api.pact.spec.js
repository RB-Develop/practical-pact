import path from "path";
import {Pact} from "@pact-foundation/pact";
import {API} from "./api";
import {eachLike, like} from "@pact-foundation/pact/dsl/matchers";

const provider = new Pact({
    consumer: 'FrontendWebsite',
    provider: 'ProductService',
    log: path.resolve(process.cwd(), 'logs', 'pact.log'),
    logLevel: "warn",
    dir: path.resolve(process.cwd(), 'pacts'),
    spec: 2
});

describe("API Pact test", () => {
    beforeAll(() => provider.setup());
    afterEach(() => provider.verify());
    afterAll(() => provider.finalize());

    describe("getting all products", () => {
        test("products exists", async () => {

            // set up Pact interactions
            await provider.addInteraction({
                state: 'products exist',
                uponReceiving: 'get all products',
                withRequest: {
                    method: 'GET',
                    path: '/products',
                    headers: {
                        "Authorization": like("Bearer 2019-01-14T11:34:18.045Z")
                    }
                },
                willRespondWith: {
                    status: 200,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: eachLike({
                        id: "1",
                        name: "Gem Visa",
                        price: 10
                    }),
                },
            });

            const api = new API(provider.mockService.baseUrl);

            // make request to Pact mock server
            const product = await api.getAllProducts();

            expect(product).toStrictEqual([
                {"id": "1", "name": "Gem Visa", "price": 10}
            ]);
        });
    });

    describe("getting one product", () => {
        test("ID 10 exists", async () => {
            // set up Pact interactions
            await provider.addInteraction({
                state: 'product with name speaker exists',
                uponReceiving: 'get product with name speaker',
                withRequest: {
                    method: 'GET',
                    path: '/products/speaker',
                    headers: {
                        "Authorization": like("Bearer 2019-01-14T11:34:18.045Z")
                    }
                },
                willRespondWith: {
                    status: 200,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: like({
                        id: "1",
                        name: "speaker",
                        price: 200
                    }),
                },
            });

            const api = new API(provider.mockService.baseUrl);

            // make request to Pact mock server
            const product = await api.getProduct("speaker");

            expect(product).toStrictEqual({
                id: "1",
                name: "speaker",
                price: 200
            });
        });

        test("product does not exist", async () => {

            // set up Pact interactions
            await provider.addInteraction({
                state: 'product with name not-exists does not exist',
                uponReceiving: 'get product with name not-exists',
                withRequest: {
                    method: 'GET',
                    path: '/products/not-exists',
                    headers: {
                        "Authorization": like("Bearer 2019-01-14T11:34:18.045Z")
                    }
                },
                willRespondWith: {
                    status: 500
                },
            });

            const api = new API(provider.mockService.baseUrl);

            // make request to Pact mock server
            await expect(api.getProduct("not-exists")).rejects.toThrow("Request failed with status code 500");
        });
    });
});