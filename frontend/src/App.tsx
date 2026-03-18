import { useEffect, useState } from 'react';
import { Card, Button, InputNumber, message, Row, Col, Statistic } from 'antd';
import { ShoppingCartOutlined } from '@ant-design/icons';
import { getProducts, createOrder, type Product } from './api';

function App() {
  const [products, setProducts] = useState<Product[]>([]);
  const [cart, setCart] = useState<Record<number, number>>({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    loadProducts();
  }, []);

  const loadProducts = async () => {
    try {
      const data = await getProducts();
      setProducts(data);
    } catch (error) {
      message.error('加载商品失败');
    }
  };

  const handleQuantityChange = (productId: number, quantity: number | null) => {
    if (quantity && quantity > 0) {
      setCart({ ...cart, [productId]: quantity });
    } else {
      const newCart = { ...cart };
      delete newCart[productId];
      setCart(newCart);
    }
  };

  const handleSubmitOrder = async () => {
    const items = Object.entries(cart).map(([productId, quantity]) => ({
      productId: Number(productId),
      quantity,
    }));

    if (items.length === 0) {
      message.warning('请先选择商品');
      return;
    }

    setLoading(true);
    try {
      await createOrder({
        userId: 1, // 写死用户ID，实际应该从登录状态获取
        items,
      });
      message.success('下单成功！');
      setCart({});
      loadProducts(); // 重新加载商品（库存会变化）
    } catch (error) {
      message.error('下单失败');
    } finally {
      setLoading(false);
    }
  };

  const totalAmount = products.reduce((sum, product) => {
    const quantity = cart[product.id] || 0;
    return sum + product.price * quantity;
  }, 0);

  const totalItems = Object.values(cart).reduce((sum, qty) => sum + qty, 0);

  return (
    <div style={{ padding: '24px', maxWidth: '1200px', margin: '0 auto' }}>
      <h1>电商小系统 - 商品列表</h1>

      <Row gutter={16} style={{ marginBottom: 24 }}>
        <Col span={8}>
          <Card>
            <Statistic title="已选商品" value={totalItems} suffix="件" />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Statistic title="总金额" value={totalAmount} precision={2} prefix="¥" />
          </Card>
        </Col>
        <Col span={8}>
          <Card>
            <Button
              type="primary"
              size="large"
              block
              icon={<ShoppingCartOutlined />}
              onClick={handleSubmitOrder}
              loading={loading}
              disabled={totalItems === 0}
            >
              提交订单
            </Button>
          </Card>
        </Col>
      </Row>

      <Row gutter={[16, 16]}>
        {products.map((product) => (
          <Col key={product.id} xs={24} sm={12} md={8} lg={6}>
            <Card
              hoverable
              cover={
                <div style={{
                  height: 200,
                  background: '#f0f0f0',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  fontSize: 48
                }}>
                  📦
                </div>
              }
            >
              <Card.Meta
                title={product.name}
                description={
                  <>
                    <p style={{ color: '#ff4d4f', fontSize: 20, fontWeight: 'bold', margin: '8px 0' }}>
                      ¥{product.price}
                    </p>
                    <p style={{ color: '#999', margin: '4px 0' }}>
                      库存: {product.stock}
                    </p>
                    <p style={{ color: '#666', margin: '8px 0', minHeight: 40 }}>
                      {product.description}
                    </p>
                    <InputNumber
                      min={0}
                      max={product.stock}
                      value={cart[product.id] || 0}
                      onChange={(val) => handleQuantityChange(product.id, val)}
                      style={{ width: '100%' }}
                      placeholder="购买数量"
                    />
                  </>
                }
              />
            </Card>
          </Col>
        ))}
      </Row>
    </div>
  );
}

export default App;
